package com.yarmook.realstate.service.ai;

import com.yarmook.realstate.config.ApplicationProperties;
import com.yarmook.realstate.domain.Facility;
import com.yarmook.realstate.domain.MapPoint;
import com.yarmook.realstate.domain.PaymentPlan;
import com.yarmook.realstate.domain.PaymentPlanItem;
import com.yarmook.realstate.domain.Property;
import com.yarmook.realstate.domain.UnitAvailability;
import com.yarmook.realstate.domain.UnitBlock;
import com.yarmook.realstate.repository.FacilityRepository;
import com.yarmook.realstate.repository.MapPointRepository;
import com.yarmook.realstate.repository.PaymentPlanItemRepository;
import com.yarmook.realstate.repository.PaymentPlanRepository;
import com.yarmook.realstate.repository.PropertyRepository;
import com.yarmook.realstate.repository.UnitAvailabilityRepository;
import com.yarmook.realstate.repository.UnitBlockRepository;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class PropertyEmbeddingService {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyEmbeddingService.class);
    private static final Pattern BEDROOM_PATTERN = Pattern.compile("(\\d+)");
    private static final DecimalFormat AREA_FORMAT = new DecimalFormat("#,##0");

    private final ApplicationProperties applicationProperties;
    private final PropertyRepository propertyRepository;
    private final FacilityRepository facilityRepository;
    private final MapPointRepository mapPointRepository;
    private final UnitBlockRepository unitBlockRepository;
    private final UnitAvailabilityRepository unitAvailabilityRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final PaymentPlanItemRepository paymentPlanItemRepository;
    private final OpenAiClient openAiClient;
    private final QdrantClient qdrantClient;

    public PropertyEmbeddingService(
        ApplicationProperties applicationProperties,
        PropertyRepository propertyRepository,
        FacilityRepository facilityRepository,
        MapPointRepository mapPointRepository,
        UnitBlockRepository unitBlockRepository,
        UnitAvailabilityRepository unitAvailabilityRepository,
        PaymentPlanRepository paymentPlanRepository,
        PaymentPlanItemRepository paymentPlanItemRepository,
        OpenAiClient openAiClient,
        QdrantClient qdrantClient
    ) {
        this.applicationProperties = applicationProperties;
        this.propertyRepository = propertyRepository;
        this.facilityRepository = facilityRepository;
        this.mapPointRepository = mapPointRepository;
        this.unitBlockRepository = unitBlockRepository;
        this.unitAvailabilityRepository = unitAvailabilityRepository;
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentPlanItemRepository = paymentPlanItemRepository;
        this.openAiClient = openAiClient;
        this.qdrantClient = qdrantClient;
    }

    @Transactional(readOnly = true)
    public void reindexAllProperties() {
        ApplicationProperties.Ai ai = applicationProperties.getAi();
        if (!ai.isEnabled() || !ai.getIndexer().isEnabled()) {
            LOG.debug("AI indexing disabled via configuration, skipping run");
            return;
        }
        if (!StringUtils.hasText(ai.getOpenAi().getApiKey())) {
            LOG.warn("OpenAI API key missing. Skip property embeddings indexing.");
            return;
        }
        int pageSize = Optional.ofNullable(ai.getIndexer().getPageSize()).orElse(50);
        int batchSize = Optional.ofNullable(ai.getQdrant().getBatchSize()).orElse(32);
        int pageNumber = 0;
        Page<Property> page;
        do {
            page = propertyRepository.findAll(PageRequest.of(pageNumber, pageSize));
            LOG.info("Indexing property embeddings page {} ({} items)", pageNumber + 1, page.getNumberOfElements());
            for (Property property : page.getContent()) {
                try {
                    indexProperty(property, batchSize);
                } catch (Exception e) {
                    LOG.error("Failed to index property extId={} id={}", property.getExtId(), property.getId(), e);
                }
            }
            pageNumber++;
        } while (page.hasNext());
    }

    private void indexProperty(Property property, int batchSize) {
        Long extId = property.getExtId();
        if (extId == null) {
            LOG.warn("Property {} is missing extId; skipping AI indexing", property.getId());
            return;
        }

        List<Facility> facilities = facilityRepository.findAllByProperty_Id(property.getId());
        List<MapPoint> mapPoints = mapPointRepository.findAllByProperty_Id(property.getId());
        List<UnitBlock> unitBlocks = unitBlockRepository.findAllByProperty_Id(property.getId());
        List<UnitAvailability> unitAvailabilities = unitAvailabilityRepository.findAllByProperty_Id(property.getId());
        List<PaymentPlan> paymentPlans = paymentPlanRepository.findAllByProperty_Id(property.getId());
        Map<Long, List<PaymentPlanItem>> planItems = fetchPaymentPlanItems(paymentPlans);

        UnitSummary unitSummary = buildUnitSummary(property, unitBlocks, unitAvailabilities);
        String titleLine = buildTitleLine(property);
        List<PropertyChunk> chunks = buildChunks(property, titleLine, facilities, mapPoints, unitSummary, paymentPlans, planItems);

        if (chunks.isEmpty()) {
            LOG.warn("Property extId={} has no meaningful content to index; clearing existing vectors", extId);
            qdrantClient.deleteByExtId(extId);
            return;
        }

        Map<String, Object> baseMetadata = buildBaseMetadata(property, unitSummary, facilities);
        List<QdrantClient.QdrantPoint> points = embedChunks(extId, chunks, baseMetadata, batchSize);
        qdrantClient.replacePoints(extId, points);
        LOG.debug("Indexed {} chunks for property extId={}", chunks.size(), extId);
    }

    private Map<Long, List<PaymentPlanItem>> fetchPaymentPlanItems(Collection<PaymentPlan> plans) {
        if (CollectionUtils.isEmpty(plans)) {
            return Collections.emptyMap();
        }
        Set<Long> planIds = plans.stream().map(PaymentPlan::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(planIds)) {
            return Collections.emptyMap();
        }
        return paymentPlanItemRepository
            .findAllByPlan_IdIn(planIds)
            .stream()
            .collect(Collectors.groupingBy(item -> item.getPlan().getId()));
    }

    private UnitSummary buildUnitSummary(Property property, List<UnitBlock> blocks, List<UnitAvailability> availability) {
        Double minArea = property.getMinArea();
        Double maxArea = property.getMaxArea();
        Double minPrice = property.getMinPriceAed() != null ? property.getMinPriceAed().doubleValue() : null;
        Double maxPrice = property.getMaxPrice() != null ? property.getMaxPrice().doubleValue() : null;
        Integer bedroomsMin = null;
        Integer bedroomsMax = null;
        String currency = StringUtils.hasText(property.getPriceCurrency()) ? property.getPriceCurrency() : "AED";

        List<String> blockDetails = new ArrayList<>();
        for (UnitBlock block : blocks) {
            if (block.getUnitsAreaFrom() != null) {
                minArea = min(minArea, block.getUnitsAreaFrom());
            }
            if (block.getUnitsAreaTo() != null) {
                maxArea = max(maxArea, block.getUnitsAreaTo());
            }
            if (block.getUnitsPriceFrom() != null) {
                minPrice = min(minPrice, toDouble(block.getUnitsPriceFrom()));
            }
            if (block.getUnitsPriceTo() != null) {
                maxPrice = max(maxPrice, toDouble(block.getUnitsPriceTo()));
            }
            if (StringUtils.hasText(block.getPriceCurrency())) {
                currency = block.getPriceCurrency();
            }
            Integer bedrooms = deduceBedrooms(block.getBedroomsAmount(), block.getUnitBedrooms());
            if (bedrooms != null) {
                bedroomsMin = min(bedroomsMin, bedrooms);
                bedroomsMax = max(bedroomsMax, bedrooms);
            }
            blockDetails.add(formatBlockDetail(block));
        }

        List<String> availabilityDetails = availability.stream().map(this::formatAvailabilityDetail).toList();

        String bedroomLabel = buildBedroomLabel(bedroomsMin, bedroomsMax);
        String areaLabel = buildAreaLabel(minArea, maxArea);
        String priceLabel = buildPriceLabel(minPrice, maxPrice, currency);

        List<String> summaryTokens = new ArrayList<>();
        if (StringUtils.hasText(bedroomLabel)) {
            summaryTokens.add(bedroomLabel);
        }
        if (StringUtils.hasText(areaLabel)) {
            summaryTokens.add(areaLabel);
        }
        if (StringUtils.hasText(priceLabel)) {
            summaryTokens.add(priceLabel);
        }
        String rangeSummary = String.join(", ", summaryTokens);

        return new UnitSummary(
            minArea,
            maxArea,
            bedroomsMin,
            bedroomsMax,
            minPrice,
            maxPrice,
            currency,
            rangeSummary,
            blockDetails,
            availabilityDetails,
            priceLabel
        );
    }

    private String buildTitleLine(Property property) {
        List<String> tokens = new ArrayList<>();
        if (StringUtils.hasText(property.getName())) {
            tokens.add(property.getName());
        }
        if (StringUtils.hasText(property.getDeveloper())) {
            tokens.add(property.getDeveloper());
        }
        if (StringUtils.hasText(property.getArea())) {
            tokens.add(property.getArea());
        } else if (StringUtils.hasText(property.getCity())) {
            tokens.add(property.getCity());
        }
        String titleBase = String.join(" – ", tokens);
        String status = null;
        if (property.getStatus() != null) {
            status = humanize(property.getStatus().name());
        } else if (StringUtils.hasText(property.getSaleStatus())) {
            status = humanize(property.getSaleStatus());
        }
        return status != null ? titleBase + " (" + status + ")" : titleBase;
    }

    private List<PropertyChunk> buildChunks(
        Property property,
        String titleLine,
        List<Facility> facilities,
        List<MapPoint> mapPoints,
        UnitSummary unitSummary,
        List<PaymentPlan> paymentPlans,
        Map<Long, List<PaymentPlanItem>> planItems
    ) {
        List<PropertyChunk> chunks = new ArrayList<>();
        Map<String, Integer> typeCounter = new HashMap<>();
        int sequence = 0;

        if (StringUtils.hasText(titleLine)) {
            chunks.add(new PropertyChunk(sequence++, "TITLE", typeCounter.compute("TITLE", (k, v) -> v == null ? 0 : v + 1), titleLine));
        }

        List<String> overviewChunks = chunkText(cleanMarkdown(property.getOverviewMd()), 900, 150);
        for (String chunk : overviewChunks) {
            chunks.add(new PropertyChunk(sequence++, "OVERVIEW", typeCounter.compute("OVERVIEW", (k, v) -> v == null ? 0 : v + 1), chunk));
        }

        List<String> facilityNames = facilities.stream().map(Facility::getName).filter(StringUtils::hasText).toList();
        if (!facilityNames.isEmpty()) {
            String facilitiesChunk = "Facilities: " + String.join("; ", facilityNames);
            chunks.add(
                new PropertyChunk(
                    sequence++,
                    "FACILITIES",
                    typeCounter.compute("FACILITIES", (k, v) -> v == null ? 0 : v + 1),
                    facilitiesChunk
                )
            );
        }

        List<String> mapDetails = mapPoints
            .stream()
            .sorted(Comparator.comparing(mp -> mp.getDistanceKm() != null ? mp.getDistanceKm() : Double.MAX_VALUE))
            .map(this::formatMapPoint)
            .filter(StringUtils::hasText)
            .toList();
        if (!mapDetails.isEmpty()) {
            String mapChunk = "Nearby: " + String.join("; ", mapDetails);
            chunks.add(new PropertyChunk(sequence++, "MAP", typeCounter.compute("MAP", (k, v) -> v == null ? 0 : v + 1), mapChunk));
        }

        List<String> paymentPlanChunks = buildPaymentPlanChunks(paymentPlans, planItems);
        for (String paymentChunk : paymentPlanChunks) {
            chunks.add(
                new PropertyChunk(
                    sequence++,
                    "PAYMENT_PLAN",
                    typeCounter.compute("PAYMENT_PLAN", (k, v) -> v == null ? 0 : v + 1),
                    paymentChunk
                )
            );
        }

        List<String> unitChunks = buildUnitSummaryChunks(unitSummary);
        for (String unitChunk : unitChunks) {
            chunks.add(
                new PropertyChunk(
                    sequence++,
                    "AVAILABILITY",
                    typeCounter.compute("AVAILABILITY", (k, v) -> v == null ? 0 : v + 1),
                    unitChunk
                )
            );
        }

        return chunks.stream().filter(chunk -> StringUtils.hasText(chunk.text())).toList();
    }

    private List<String> buildPaymentPlanChunks(List<PaymentPlan> paymentPlans, Map<Long, List<PaymentPlanItem>> planItems) {
        if (CollectionUtils.isEmpty(paymentPlans)) {
            return Collections.emptyList();
        }
        List<String> lines = new ArrayList<>();
        for (PaymentPlan plan : paymentPlans) {
            List<PaymentPlanItem> items = new ArrayList<>(planItems.getOrDefault(plan.getId(), Collections.emptyList()));
            items.sort(Comparator.comparing(PaymentPlanItem::getOrderNo, Comparator.nullsLast(Integer::compareTo)));
            List<String> itemLines = new ArrayList<>();
            for (PaymentPlanItem item : items) {
                List<String> tokens = new ArrayList<>();
                if (StringUtils.hasText(item.getPaymentTime())) {
                    tokens.add(item.getPaymentTime());
                }
                if (StringUtils.hasText(item.getPercentOfPayment())) {
                    tokens.add(item.getPercentOfPayment());
                }
                if (!tokens.isEmpty()) {
                    itemLines.add(String.join(" ", tokens));
                }
            }
            String flattenedItems = itemLines.isEmpty() ? "Details unavailable" : String.join(" | ", itemLines);
            lines.add(plan.getName() + ": " + flattenedItems);
        }
        String joined = "Payment plans: " + String.join("; ", lines);
        return chunkText(joined, 900, 150);
    }

    private List<String> buildUnitSummaryChunks(UnitSummary unitSummary) {
        if (unitSummary == null) {
            return Collections.emptyList();
        }
        List<String> lines = new ArrayList<>();
        if (StringUtils.hasText(unitSummary.rangeSummary())) {
            lines.add("Unit summary: " + unitSummary.rangeSummary());
        }
        if (!CollectionUtils.isEmpty(unitSummary.blockDetails())) {
            lines.add("Unit blocks: " + String.join(" | ", unitSummary.blockDetails()));
        }
        if (!CollectionUtils.isEmpty(unitSummary.availabilityDetails())) {
            lines.add("Availability: " + String.join(" | ", unitSummary.availabilityDetails()));
        }
        if (lines.isEmpty()) {
            return Collections.emptyList();
        }
        return chunkText(String.join(" ", lines), 900, 150);
    }

    private Map<String, Object> buildBaseMetadata(Property property, UnitSummary unitSummary, List<Facility> facilities) {
        Map<String, Object> payload = new HashMap<>();
        putIfNotNull(payload, "extId", property.getExtId());
        putIfNotNull(payload, "propertyId", property.getId());
        putIfNotNull(payload, "slug", property.getSlug());
        putIfNotNull(payload, "name", property.getName());
        putIfNotNull(payload, "developer", property.getDeveloper());
        putIfNotNull(payload, "area", property.getArea());
        putIfNotNull(payload, "city", property.getCity());
        putIfNotNull(payload, "country", property.getCountry());
        putIfNotNull(payload, "status", property.getStatus() != null ? property.getStatus().name() : null);
        putIfNotNull(payload, "saleStatus", property.getSaleStatus());
        putIfNotNull(payload, "priceCurrency", property.getPriceCurrency());
        putIfNotNull(payload, "minPriceAed", property.getMinPriceAed() != null ? property.getMinPriceAed().doubleValue() : null);
        putIfNotNull(payload, "minArea", property.getMinArea());
        putIfNotNull(payload, "maxArea", property.getMaxArea());
        putIfNotNull(payload, "updatedAt", property.getUpdatedAt() != null ? property.getUpdatedAt().toString() : null);
        if (unitSummary != null) {
            putIfNotNull(payload, "bedroomsMin", unitSummary.bedroomsMin());
            putIfNotNull(payload, "bedroomsMax", unitSummary.bedroomsMax());
            putIfNotNull(payload, "unitsRange", unitSummary.rangeSummary());
            putIfNotNull(payload, "priceRange", unitSummary.priceLabel());
        }
        if (!CollectionUtils.isEmpty(facilities)) {
            Set<String> facilityNames = facilities.stream().map(Facility::getName).filter(StringUtils::hasText).collect(Collectors.toSet());
            if (!facilityNames.isEmpty()) {
                payload.put("facilities", facilityNames);
            }
        }
        payload.put("source", "property");
        return payload;
    }

    private List<QdrantClient.QdrantPoint> embedChunks(
        Long extId,
        List<PropertyChunk> chunks,
        Map<String, Object> baseMetadata,
        int batchSize
    ) {
        List<QdrantClient.QdrantPoint> points = new ArrayList<>();
        for (int start = 0; start < chunks.size(); start += batchSize) {
            int end = Math.min(start + batchSize, chunks.size());
            List<PropertyChunk> batch = chunks.subList(start, end);
            List<String> texts = batch.stream().map(PropertyChunk::text).toList();
            List<float[]> embeddings = openAiClient.embed(texts);
            if (embeddings.size() != batch.size()) {
                throw new IllegalStateException(
                    "Embeddings count %d does not match chunk count %d".formatted(embeddings.size(), batch.size())
                );
            }
            for (int i = 0; i < batch.size(); i++) {
                PropertyChunk chunk = batch.get(i);
                float[] vector = embeddings.get(i);
                Map<String, Object> payload = new HashMap<>(baseMetadata);
                payload.put("chunkText", chunk.text());
                payload.put("chunkType", chunk.type());
                payload.put("chunkIndex", chunk.typeIndex());
                payload.put("sequence", chunk.sequence());
                points.add(new QdrantClient.QdrantPoint(buildPointId(extId, chunk), vector, payload));
            }
        }
        return points;
    }

    private String buildPointId(Long extId, PropertyChunk chunk) {
        return "property-%s-%s-%03d".formatted(extId, chunk.type().toLowerCase(Locale.ROOT), chunk.sequence());
    }

    private String formatMapPoint(MapPoint mapPoint) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.hasText(mapPoint.getName())) {
            builder.append(mapPoint.getName());
        }
        if (mapPoint.getDistanceKm() != null) {
            builder.append(" (").append(String.format(Locale.ENGLISH, "%.2f km", mapPoint.getDistanceKm())).append(")");
        }
        return builder.toString();
    }

    private String formatBlockDetail(UnitBlock block) {
        List<String> tokens = new ArrayList<>();
        if (StringUtils.hasText(block.getUnitType())) {
            tokens.add(block.getUnitType());
        } else if (StringUtils.hasText(block.getNormalizedType())) {
            tokens.add(block.getNormalizedType());
        }
        if (StringUtils.hasText(block.getBedroomsAmount())) {
            tokens.add(block.getBedroomsAmount());
        }
        String areaRange = buildAreaLabel(block.getUnitsAreaFrom(), block.getUnitsAreaTo());
        if (StringUtils.hasText(areaRange)) {
            tokens.add(areaRange);
        }
        String priceRange = buildPriceLabel(
            block.getUnitsPriceFrom() != null ? block.getUnitsPriceFrom().doubleValue() : null,
            block.getUnitsPriceTo() != null ? block.getUnitsPriceTo().doubleValue() : null,
            block.getPriceCurrency()
        );
        if (StringUtils.hasText(priceRange)) {
            tokens.add(priceRange);
        }
        if (block.getUnitsAmount() != null) {
            tokens.add(block.getUnitsAmount() + " units");
        }
        return String.join(", ", tokens);
    }

    private String formatAvailabilityDetail(UnitAvailability availability) {
        List<String> tokens = new ArrayList<>();
        if (StringUtils.hasText(availability.getBuildingName())) {
            tokens.add(availability.getBuildingName());
        }
        if (StringUtils.hasText(availability.getBedroomType())) {
            tokens.add(availability.getBedroomType());
        }
        String areaLabel = buildAreaLabel(availability.getAreaFrom(), null);
        if (StringUtils.hasText(areaLabel)) {
            tokens.add(areaLabel);
        }
        String priceLabel = buildPriceLabel(
            availability.getPriceFrom() != null ? availability.getPriceFrom().doubleValue() : null,
            availability.getPriceTo() != null ? availability.getPriceTo().doubleValue() : null,
            availability.getPriceCurrency()
        );
        if (StringUtils.hasText(priceLabel)) {
            tokens.add(priceLabel);
        }
        if (availability.getUnitsAvailable() != null) {
            tokens.add(availability.getUnitsAvailable() + " units available");
        }
        if (availability.getLastUpdated() != null) {
            tokens.add("updated " + availability.getLastUpdated());
        }
        return String.join(", ", tokens);
    }

    private List<String> chunkText(String text, int targetTokens, int overlapTokens) {
        if (!StringUtils.hasText(text)) {
            return Collections.emptyList();
        }
        String sanitized = text.trim().replaceAll("\\s+", " ");
        if (!StringUtils.hasText(sanitized)) {
            return Collections.emptyList();
        }
        String[] words = sanitized.split(" ");
        if (words.length <= targetTokens) {
            return List.of(sanitized);
        }

        List<String> chunks = new ArrayList<>();
        int start = 0;
        int step = Math.max(targetTokens - overlapTokens, 1);
        while (start < words.length) {
            int end = Math.min(start + targetTokens, words.length);
            String chunk = String.join(" ", Arrays.copyOfRange(words, start, end));
            chunks.add(chunk);
            if (end == words.length) {
                break;
            }
            start += step;
        }
        return chunks;
    }

    private String cleanMarkdown(String markdown) {
        if (!StringUtils.hasText(markdown)) {
            return "";
        }
        String cleaned = markdown.replace("\r", "\n");
        cleaned = cleaned.replaceAll("(?m)^#{1,6}\\s*", "");
        cleaned = cleaned.replaceAll("(?m)^[-*+]\\s*", "");
        cleaned = cleaned.replaceAll("[*_`>~]", "");
        return cleaned.trim();
    }

    private Integer deduceBedrooms(String... candidates) {
        for (String candidate : candidates) {
            if (!StringUtils.hasText(candidate)) {
                continue;
            }
            String value = candidate.toLowerCase(Locale.ENGLISH);
            if (value.contains("studio")) {
                return 0;
            }
            Matcher matcher = BEDROOM_PATTERN.matcher(value);
            if (matcher.find()) {
                try {
                    return Integer.parseInt(matcher.group(1));
                } catch (NumberFormatException ignored) {
                    // ignore invalid value
                }
            }
        }
        return null;
    }

    private String buildBedroomLabel(Integer min, Integer max) {
        if (min == null && max == null) {
            return null;
        }
        if (min != null && max != null) {
            if (Objects.equals(min, max)) {
                return formatBedroom(min);
            }
            return formatBedroom(min) + "–" + formatBedroom(max);
        }
        return min != null ? formatBedroom(min) : formatBedroom(max);
    }

    private String formatBedroom(Integer value) {
        if (value == null) {
            return "";
        }
        if (value == 0) {
            return "Studio";
        }
        return value + "BR";
    }

    private String buildAreaLabel(Double minArea, Double maxArea) {
        if (minArea == null && maxArea == null) {
            return null;
        }
        if (minArea != null && maxArea != null) {
            if (Objects.equals(Math.round(minArea), Math.round(maxArea))) {
                return AREA_FORMAT.format(Math.round(minArea)) + " m2";
            }
            return AREA_FORMAT.format(Math.round(minArea)) + "–" + AREA_FORMAT.format(Math.round(maxArea)) + " m2";
        }
        double value = minArea != null ? minArea : maxArea;
        return AREA_FORMAT.format(Math.round(value)) + " m2";
    }

    private String buildPriceLabel(Double minPrice, Double maxPrice, String currency) {
        if (minPrice == null && maxPrice == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        if (minPrice != null) {
            builder.append("from ").append(formatPrice(minPrice));
        }
        if (maxPrice != null && !Objects.equals(maxPrice, minPrice)) {
            if (builder.length() > 0) {
                builder.append(" to ");
            }
            builder.append(formatPrice(maxPrice));
        }
        if (builder.length() == 0) {
            builder.append(formatPrice(maxPrice != null ? maxPrice : minPrice));
        }
        if (StringUtils.hasText(currency)) {
            builder.append(" ").append(currency);
        }
        return builder.toString().trim();
    }

    private String formatPrice(Double value) {
        if (value == null) {
            return "";
        }
        double abs = Math.abs(value);
        if (abs >= 1_000_000_000) {
            return String.format(Locale.ENGLISH, "%.2fB", value / 1_000_000_000d);
        }
        if (abs >= 1_000_000) {
            return String.format(Locale.ENGLISH, "%.2fM", value / 1_000_000d);
        }
        if (abs >= 1_000) {
            return String.format(Locale.ENGLISH, "%.2fK", value / 1_000d);
        }
        return String.format(Locale.ENGLISH, "%.0f", value);
    }

    private double toDouble(BigDecimal value) {
        return value == null ? 0d : value.doubleValue();
    }

    private Integer min(Integer current, Integer candidate) {
        if (current == null) {
            return candidate;
        }
        if (candidate == null) {
            return current;
        }
        return Math.min(current, candidate);
    }

    private Double min(Double current, Double candidate) {
        if (current == null) {
            return candidate;
        }
        if (candidate == null) {
            return current;
        }
        return Math.min(current, candidate);
    }

    private Integer max(Integer current, Integer candidate) {
        if (current == null) {
            return candidate;
        }
        if (candidate == null) {
            return current;
        }
        return Math.max(current, candidate);
    }

    private Double max(Double current, Double candidate) {
        if (current == null) {
            return candidate;
        }
        if (candidate == null) {
            return current;
        }
        return Math.max(current, candidate);
    }

    private String humanize(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return Arrays.stream(value.replace('_', ' ').split(" "))
            .map(token -> token.substring(0, 1).toUpperCase(Locale.ENGLISH) + token.substring(1).toLowerCase(Locale.ENGLISH))
            .collect(Collectors.joining(" "));
    }

    private void putIfNotNull(Map<String, Object> payload, String key, Object value) {
        if (value != null) {
            payload.put(key, value);
        }
    }

    private record PropertyChunk(int sequence, String type, int typeIndex, String text) {}

    private record UnitSummary(
        Double minArea,
        Double maxArea,
        Integer bedroomsMin,
        Integer bedroomsMax,
        Double minPrice,
        Double maxPrice,
        String currency,
        String rangeSummary,
        List<String> blockDetails,
        List<String> availabilityDetails,
        String priceLabel
    ) {}
}
