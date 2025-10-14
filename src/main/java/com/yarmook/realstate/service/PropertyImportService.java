package com.yarmook.realstate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yarmook.realstate.config.ApplicationProperties;
import com.yarmook.realstate.domain.Property;
import com.yarmook.realstate.domain.PropertyMarker;
import com.yarmook.realstate.domain.enumeration.ListingType;
import com.yarmook.realstate.domain.enumeration.PropertyStatus;
import com.yarmook.realstate.repository.PropertyMarkerRepository;
import com.yarmook.realstate.repository.PropertyRepository;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PropertyImportService {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyImportService.class);

    private static final int DEFAULT_PER_PAGE = 50;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ApplicationProperties applicationProperties;
    private final PropertyRepository propertyRepository;
    private final PropertyMarkerRepository propertyMarkerRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public PropertyImportService(
        RestTemplateBuilder restTemplateBuilder,
        ObjectMapper objectMapper,
        ApplicationProperties applicationProperties,
        PropertyRepository propertyRepository,
        PropertyMarkerRepository propertyMarkerRepository,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.applicationProperties = applicationProperties;
        this.propertyRepository = propertyRepository;
        this.propertyMarkerRepository = propertyMarkerRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public PropertyRefreshResult refreshFromExternal() {
        ApplicationProperties.External.Reely reely = applicationProperties.getExternal().getReely();
        if (!reely.isEnabled()) {
            throw new IllegalStateException("Reely integration is disabled via configuration");
        }
        if (!StringUtils.hasText(reely.getBaseUrl())) {
            throw new IllegalStateException("Reely base URL is not configured");
        }

        Instant syncTime = Instant.now();

        List<JsonNode> markerNodes = fetchPropertyMarkers(reely);
        MarkerSyncStats markerStats = syncMarkers(markerNodes);

        List<JsonNode> propertyNodes = fetchProperties(reely);
        PropertySyncStats propertyStats = syncProperties(propertyNodes, markerStats.markersByExtId(), syncTime, reely);

        LOG.info(
            "Property refresh completed: {} created, {} updated, {} skipped; markers created: {}, updated: {}",
            propertyStats.created(),
            propertyStats.updated(),
            propertyStats.skipped(),
            markerStats.created(),
            markerStats.updated()
        );

        return new PropertyRefreshResult(
            propertyStats.created(),
            propertyStats.updated(),
            propertyStats.skipped(),
            markerStats.created(),
            markerStats.updated(),
            syncTime
        );
    }

    private List<JsonNode> fetchPropertyMarkers(ApplicationProperties.External.Reely reely) {
        JsonNode root = fetchPage(reely, reely.getPropertyMarkersPath(), null);
        JsonNode markers = extractArray(root, "markers");
        if (markers == null || !markers.isArray()) {
            throw new IllegalStateException("Unexpected payload when fetching property markers");
        }
        List<JsonNode> result = new ArrayList<>();
        markers.forEach(result::add);
        return result;
    }

    private List<JsonNode> fetchProperties(ApplicationProperties.External.Reely reely) {
        List<JsonNode> result = new ArrayList<>();
        int page = 1;
        while (true) {
            JsonNode root = fetchPage(reely, reely.getPropertiesPath(), page);
            JsonNode items = extractArray(root, "items");
            if (items == null || !items.isArray() || items.isEmpty()) {
                break;
            }
            items.forEach(result::add);
            JsonNode pagination = root.path("pagination");
            boolean hasNext = pagination.path("has_next").asBoolean(false);
            int totalPages = Math.max(pagination.path("pages").asInt(page), page);
            if (!hasNext || page >= totalPages) {
                break;
            }
            page++;
        }
        return result;
    }

    private JsonNode fetchPage(ApplicationProperties.External.Reely reely, String path, Integer page) {
        URI uri = buildUri(reely.getBaseUrl(), path, reely.getPageSize(), page);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (StringUtils.hasText(reely.getApiKey())) {
            headers.set(reely.getApiKeyHeader(), reely.getApiKey());
        }
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
        } catch (RestClientException ex) {
            throw new IllegalStateException("Failed to fetch data from " + uri, ex);
        }
        if (response.getBody() == null) {
            throw new IllegalStateException("Empty response body from " + uri);
        }
        try {
            return objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to parse JSON response from " + uri, e);
        }
    }

    private URI buildUri(String baseUrl, String path, Integer configuredPerPage, Integer page) {
        String normalizedBase = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        String normalizedPath = (path == null || path.isBlank()) ? "" : (path.startsWith("/") ? path : "/" + path);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(normalizedBase + normalizedPath);
        int perPage = (configuredPerPage != null && configuredPerPage > 0)
            ? Math.min(configuredPerPage, DEFAULT_PER_PAGE)
            : DEFAULT_PER_PAGE;
        builder.queryParam("per_page", perPage);
        if (page != null && page > 0) {
            builder.queryParam("page", page);
        }
        return builder.build().toUri();
    }

    private MarkerSyncStats syncMarkers(List<JsonNode> markerNodes) {
        Map<Long, PropertyMarker> markersByExtId = new HashMap<>();
        AtomicInteger created = new AtomicInteger();
        AtomicInteger updated = new AtomicInteger();

        for (JsonNode node : markerNodes) {
            Long extId = readLong(node, "id", "extId", "propertyId", "property_id");
            if (extId == null) {
                LOG.debug("Skipping marker without extId: {}", node);
                continue;
            }

            PropertyMarker marker = propertyMarkerRepository
                .findOneByExtId(extId)
                .orElseGet(() -> {
                    PropertyMarker m = new PropertyMarker();
                    m.setExtId(extId);
                    created.incrementAndGet();
                    return m;
                });
            if (marker.getId() != null) {
                updated.incrementAndGet();
            }

            Double latitude = readDouble(node, "latitude", "lat");
            Double longitude = readDouble(node, "longitude", "lon", "lng");
            if (latitude == null || longitude == null) {
                Double[] coords = parseCoordinatePair(readText(node, "coordinates", "location", "geo", "geometry"));
                if (coords != null) {
                    latitude = coords[0];
                    longitude = coords[1];
                }
            }
            marker.setLatitude(latitude);
            marker.setLongitude(longitude);
            marker.setArea(readText(node, "area", "community"));
            marker.setCompletionDate(parseLocalDate(readText(node, "completionDate", "completion_date")));
            marker.setName(readText(node, "name", "title"));
            marker.setDeveloper(readText(node, "developer"));
            marker.setStatus(readText(node, "status", "state"));
            marker.setSaleStatus(readText(node, "saleStatus", "sale_status"));
            marker.setMinPrice(readBigDecimal(node, "minPrice", "min_price"));

            String coverPayload = readText(node, "coverJson", "cover", "cover_image_url");
            marker.setCoverJson(coverPayload);
            marker.setCoverUrl(extractMediaUrl(coverPayload));

            if (marker.getId() == null) {
                marker.setId(sequenceGeneratorService.generateSequence(PropertyMarker.SEQUENCE_NAME));
            }
            marker = propertyMarkerRepository.save(marker);
            markersByExtId.put(extId, marker);
        }

        return new MarkerSyncStats(created.get(), updated.get(), markersByExtId);
    }

    private PropertySyncStats syncProperties(
        List<JsonNode> propertyNodes,
        Map<Long, PropertyMarker> markersByExtId,
        Instant syncTime,
        ApplicationProperties.External.Reely reely
    ) {
        AtomicInteger created = new AtomicInteger();
        AtomicInteger updated = new AtomicInteger();
        AtomicInteger skipped = new AtomicInteger();

        for (JsonNode listNode : propertyNodes) {
            Long extId = readLong(listNode, "id", "extId", "propertyId", "property_id");
            JsonNode detailNode = fetchPropertyDetail(reely, extId);
            JsonNode source = mergeNodes(listNode, detailNode);
            String name = readText(source, "name", "title", "displayName");
            if (extId == null || !StringUtils.hasText(name)) {
                skipped.incrementAndGet();
                LOG.debug("Skipping property due to missing extId/name: {}", source);
                continue;
            }

            Property property = propertyRepository
                .findOneByExtId(extId)
                .orElseGet(() -> {
                    Property p = new Property();
                    p.setExtId(extId);
                    created.incrementAndGet();
                    return p;
                });
            if (property.getId() != null) {
                updated.incrementAndGet();
            }

            property.setName(name.trim());
            String slug = readText(source, "slug", "slugName", "slug_name", "projectSlug", "permalink");
            if (StringUtils.hasText(slug)) {
                property.setSlug(slug);
            }
            property.setDeveloper(readText(source, "developer", "developerName"));
            property.setArea(readText(source, "area", "community", "neighbourhood"));
            property.setCity(readText(source, "city", "cityName"));
            property.setCountry(readText(source, "country", "countryName"));
            property.setListingType(parseListingType(readText(source, "listingType", "listing_type", "type")));
            property.setStatus(parsePropertyStatus(readText(source, "status", "state")));
            property.setSaleStatus(readText(source, "saleStatus", "sale_status"));
            property.setReadiness(readText(source, "readiness", "readinessStatus"));
            property.setServiceCharge(readText(source, "serviceCharge", "service_charge"));
            property.setFurnishing(readText(source, "furnishing", "furnishingStatus"));
            property.setHasEscrow(readBoolean(source, "hasEscrow", "escrow", "has_escrow"));
            property.setPostHandover(readBoolean(source, "postHandover", "post_handover"));
            property.setCompletionDateTime(
                parseInstant(
                    readText(source, "completionDateTime", "handoverDate", "completion_date_time", "handover_date", "completion_datetime")
                )
            );
            property.setMinPrice(readBigDecimal(source, "minPrice", "min_price", "priceFrom", "price_from"));
            property.setMaxPrice(readBigDecimal(source, "maxPrice", "max_price", "priceTo", "price_to"));
            property.setMinPriceAed(readBigDecimal(source, "minPriceAed", "min_price_aed"));
            property.setPriceCurrency(readText(source, "priceCurrency", "currency"));
            property.setMinArea(readDouble(source, "minArea", "min_area", "areaFrom", "area_from"));
            property.setMaxArea(readDouble(source, "maxArea", "max_area", "areaTo", "area_to"));
            property.setAreaUnit(readText(source, "areaUnit", "area_unit", "areaType"));

            Double latitude = readDouble(source, "latitude", "lat", "location.latitude");
            Double longitude = readDouble(source, "longitude", "lon", "lng", "location.longitude", "location.lon", "location.lng");
            if (latitude == null || longitude == null) {
                Double[] coords = parseCoordinatePair(readText(source, "coordinates"));
                if (coords != null) {
                    latitude = coords[0];
                    longitude = coords[1];
                }
            }
            property.setLatitude(latitude);
            property.setLongitude(longitude);

            String coverPayload = readText(source, "cover_image_url", "coverJson", "coverUrl", "cover", "heroImage", "image");
            property.setCoverJson(coverPayload);
            property.setCoverUrl(extractMediaUrl(coverPayload));
            property.setVideoUrl(readText(source, "videoUrl", "video", "video_url"));
            property.setBrochureUrl(readText(source, "brochureUrl", "brochure", "brochure_url"));
            property.setLayoutsPdfUrl(readText(source, "layoutsPdfUrl", "layouts", "layouts_pdf_url"));
            property.setWebsite(readText(source, "website", "url", "link"));
            property.setOverviewMd(readText(source, "overviewMd", "description", "summary", "overview"));
            property.setBuildingsJson(toJson(resolve(source, "buildings")));
            property.setRaw(source.toString());
            property.setPublishedAt(parseInstant(readText(source, "publishedAt", "published_at", "publishedDate")));
            property.setUpdatedAt(syncTime);

            PropertyMarker marker = markersByExtId.get(extId);
            if (marker != null) {
                property.setMarker(marker);
                marker.setProperty(property);
                if (marker.getId() == null) {
                    marker.setId(sequenceGeneratorService.generateSequence(PropertyMarker.SEQUENCE_NAME));
                }
                propertyMarkerRepository.save(marker);
            }

            if (property.getId() == null) {
                property.setId(sequenceGeneratorService.generateSequence(Property.SEQUENCE_NAME));
            }
            propertyRepository.save(property);
        }

        return new PropertySyncStats(created.get(), updated.get(), skipped.get());
    }

    private JsonNode fetchPropertyDetail(ApplicationProperties.External.Reely reely, Long extId) {
        if (extId == null) {
            return null;
        }
        String detailPath = resolveDetailPath(reely, extId);
        JsonNode root = fetchResource(reely, detailPath);
        if (root == null) {
            return null;
        }
        if (root.has("property")) {
            root = root.get("property");
        }
        return root.isObject() ? root : null;
    }

    private String resolveDetailPath(ApplicationProperties.External.Reely reely, Long extId) {
        String template = reely.getPropertyDetailPath();
        if (StringUtils.hasText(template)) {
            if (template.contains("%s")) {
                return String.format(template, extId);
            }
            if (template.endsWith("/")) {
                return template + extId;
            }
            return template + "/" + extId;
        }
        String basePath = reely.getPropertiesPath();
        String base = (basePath == null || basePath.isBlank()) ? "/properties" : basePath;
        return base.endsWith("/") ? base + extId : base + "/" + extId;
    }

    private JsonNode fetchResource(ApplicationProperties.External.Reely reely, String path) {
        URI uri = buildResourceUri(reely.getBaseUrl(), path);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (StringUtils.hasText(reely.getApiKey())) {
            headers.set(reely.getApiKeyHeader(), reely.getApiKey());
        }
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
        } catch (RestClientException ex) {
            LOG.warn("Failed to fetch resource {}", uri, ex);
            return null;
        }
        if (response.getBody() == null) {
            return null;
        }
        try {
            return objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            LOG.warn("Unable to parse resource response from {}", uri, e);
            return null;
        }
    }

    private JsonNode mergeNodes(JsonNode baseNode, JsonNode overlayNode) {
        if (overlayNode == null || overlayNode.isNull()) {
            return baseNode;
        }
        if (baseNode == null || baseNode.isNull()) {
            return overlayNode;
        }
        ObjectNode merged = baseNode.isObject() ? ((ObjectNode) baseNode).deepCopy() : objectMapper.createObjectNode();
        overlayNode.fields().forEachRemaining(entry -> merged.set(entry.getKey(), entry.getValue()));
        return merged;
    }

    private URI buildResourceUri(String baseUrl, String path) {
        String normalizedBase = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        String normalizedPath = (path == null || path.isBlank()) ? "" : (path.startsWith("/") ? path : "/" + path);
        return UriComponentsBuilder.fromHttpUrl(normalizedBase + normalizedPath).build().toUri();
    }

    private JsonNode extractArray(JsonNode root, String fieldName) {
        if (root == null) {
            return null;
        }
        if (root.isArray()) {
            return root;
        }
        if (root.has(fieldName)) {
            return root.get(fieldName);
        }
        return null;
    }

    private static Long readLong(JsonNode node, String... paths) {
        for (String path : paths) {
            JsonNode value = resolve(node, path);
            if (value == null || value.isNull()) {
                continue;
            }
            if (value.isIntegralNumber()) {
                return value.longValue();
            }
            if (value.isTextual()) {
                String text = value.asText();
                if (StringUtils.hasText(text)) {
                    try {
                        return Long.parseLong(text.trim());
                    } catch (NumberFormatException ignored) {
                        // ignore
                    }
                }
            }
        }
        return null;
    }

    private static Double readDouble(JsonNode node, String... paths) {
        for (String path : paths) {
            JsonNode value = resolve(node, path);
            if (value == null || value.isNull()) {
                continue;
            }
            if (value.isNumber()) {
                return value.doubleValue();
            }
            if (value.isTextual()) {
                String text = value.asText();
                if (StringUtils.hasText(text)) {
                    try {
                        return Double.parseDouble(text.trim());
                    } catch (NumberFormatException ignored) {
                        // ignore
                    }
                }
            }
        }
        return null;
    }

    private static BigDecimal readBigDecimal(JsonNode node, String... paths) {
        for (String path : paths) {
            JsonNode value = resolve(node, path);
            if (value == null || value.isNull()) {
                continue;
            }
            if (value.isNumber()) {
                return value.decimalValue();
            }
            if (value.isTextual()) {
                String text = value.asText();
                if (StringUtils.hasText(text)) {
                    try {
                        return new BigDecimal(text.trim());
                    } catch (NumberFormatException ignored) {
                        // ignore
                    }
                }
            }
        }
        return null;
    }

    private static String readText(JsonNode node, String... paths) {
        for (String path : paths) {
            JsonNode value = resolve(node, path);
            if (value == null || value.isNull()) {
                continue;
            }
            if (value.isTextual()) {
                String text = value.asText().trim();
                if (StringUtils.hasText(text)) {
                    return text;
                }
            } else if (value.isNumber()) {
                return value.asText();
            }
        }
        return null;
    }

    private static Boolean readBoolean(JsonNode node, String... paths) {
        for (String path : paths) {
            JsonNode value = resolve(node, path);
            if (value == null || value.isNull()) {
                continue;
            }
            if (value.isBoolean()) {
                return value.booleanValue();
            }
            if (value.isNumber()) {
                return value.intValue() != 0;
            }
            if (value.isTextual()) {
                String text = value.asText().trim().toLowerCase(Locale.ROOT);
                if (text.equals("true") || text.equals("yes") || text.equals("y") || text.equals("1")) {
                    return true;
                }
                if (text.equals("false") || text.equals("no") || text.equals("n") || text.equals("0")) {
                    return false;
                }
            }
        }
        return null;
    }

    private static JsonNode resolve(JsonNode node, String path) {
        if (!StringUtils.hasText(path) || node == null) {
            return null;
        }
        String[] segments = path.split("\\.");
        JsonNode current = node;
        for (String segment : segments) {
            if (current == null) {
                return null;
            }
            current = current.path(segment);
        }
        return current.isMissingNode() ? null : current;
    }

    private String extractMediaUrl(String payload) {
        if (!StringUtils.hasText(payload)) {
            return null;
        }
        String trimmed = payload.trim();
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            try {
                JsonNode mediaNode = objectMapper.readTree(trimmed);
                if (mediaNode.hasNonNull("url")) {
                    return mediaNode.get("url").asText();
                }
            } catch (JsonProcessingException ignored) {
                // ignore
            }
        }
        return null;
    }

    private String toJson(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            LOG.debug("Failed to serialise node", e);
            return node.toString();
        }
    }

    private static ListingType parseListingType(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        try {
            return ListingType.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            LOG.debug("Unknown listing type: {}", value);
            return null;
        }
    }

    private static PropertyStatus parsePropertyStatus(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return PropertyStatus.valueOf(value.trim());
        } catch (IllegalArgumentException ex) {
            LOG.debug("Unknown property status: {}", value);
            return null;
        }
    }

    private static Instant parseInstant(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String text = value.trim();
        try {
            return Instant.parse(text);
        } catch (DateTimeParseException ignored) {}
        try {
            return OffsetDateTime.parse(text).toInstant();
        } catch (DateTimeParseException ignored) {}
        try {
            LocalDate date = LocalDate.parse(text);
            return date.atStartOfDay().toInstant(ZoneOffset.UTC);
        } catch (DateTimeParseException ignored) {}
        return null;
    }

    private static LocalDate parseLocalDate(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String text = value.trim();
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException ignored) {}
        try {
            return OffsetDateTime.parse(text).toLocalDate();
        } catch (DateTimeParseException ignored) {}
        return null;
    }

    private static Double[] parseCoordinatePair(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String[] parts = value.split(",");
        if (parts.length < 2) {
            return null;
        }
        try {
            Double lat = Double.parseDouble(parts[0].trim());
            Double lon = Double.parseDouble(parts[1].trim());
            return new Double[] { lat, lon };
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private record MarkerSyncStats(int created, int updated, Map<Long, PropertyMarker> markersByExtId) {}

    private record PropertySyncStats(int created, int updated, int skipped) {}

    public static final class PropertyRefreshResult {

        private final int propertiesCreated;
        private final int propertiesUpdated;
        private final int propertiesSkipped;
        private final int markersCreated;
        private final int markersUpdated;
        private final Instant syncedAt;

        public PropertyRefreshResult(
            int propertiesCreated,
            int propertiesUpdated,
            int propertiesSkipped,
            int markersCreated,
            int markersUpdated,
            Instant syncedAt
        ) {
            this.propertiesCreated = propertiesCreated;
            this.propertiesUpdated = propertiesUpdated;
            this.propertiesSkipped = propertiesSkipped;
            this.markersCreated = markersCreated;
            this.markersUpdated = markersUpdated;
            this.syncedAt = syncedAt;
        }

        public int getPropertiesCreated() {
            return propertiesCreated;
        }

        public int getPropertiesUpdated() {
            return propertiesUpdated;
        }

        public int getPropertiesSkipped() {
            return propertiesSkipped;
        }

        public int getMarkersCreated() {
            return markersCreated;
        }

        public int getMarkersUpdated() {
            return markersUpdated;
        }

        public Instant getSyncedAt() {
            return syncedAt;
        }
    }
}
