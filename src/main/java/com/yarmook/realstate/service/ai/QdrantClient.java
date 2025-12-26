package com.yarmook.realstate.service.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yarmook.realstate.config.ApplicationProperties;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class QdrantClient {

    private static final Logger LOG = LoggerFactory.getLogger(QdrantClient.class);

    private final RestTemplate restTemplate;
    private final ApplicationProperties.Ai.Qdrant qdrantProperties;
    private final ObjectMapper objectMapper;

    public QdrantClient(RestTemplateBuilder restTemplateBuilder, ApplicationProperties applicationProperties, ObjectMapper objectMapper) {
        this.qdrantProperties = applicationProperties.getAi().getQdrant();
        this.objectMapper = objectMapper;
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            if (StringUtils.hasText(qdrantProperties.getApiKey())) {
                request.getHeaders().set("api-key", qdrantProperties.getApiKey());
            }
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return execution.execute(request, body);
        });
        interceptors.add((request, body, execution) -> {
            if (LOG.isDebugEnabled()) {
                String payload = body != null && body.length > 0 ? new String(body, StandardCharsets.UTF_8) : "<empty>";
                LOG.debug("Qdrant HTTP {} {} payload: {}", request.getMethod(), request.getURI(), payload);
            }
            return execution.execute(request, body);
        });
        this.restTemplate = restTemplateBuilder
            .rootUri(qdrantProperties.getUrl())
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(30))
            .additionalInterceptors(interceptors)
            .build();
        LOG.info(
            "Initialized Qdrant client targeting {} collection '{}' (API key configured: {})",
            qdrantProperties.getUrl(),
            qdrantProperties.getCollection(),
            StringUtils.hasText(qdrantProperties.getApiKey())
        );
    }

    public void replacePoints(Long extId, List<QdrantPoint> points) {
        LOG.debug(
            "Replacing Qdrant points for extId {} with {} new vectors (collection '{}', base URL {})",
            extId,
            CollectionUtils.isEmpty(points) ? 0 : points.size(),
            qdrantProperties.getCollection(),
            qdrantProperties.getUrl()
        );
        deleteByExtId(extId);
        upsert(points);
    }

    public void upsert(List<QdrantPoint> points) {
        if (CollectionUtils.isEmpty(points)) {
            return;
        }
        List<PointStruct> pointItems = new ArrayList<>(points.size());
        for (QdrantPoint point : points) {
            Map<String, Object> payload = normalizePayload(point.payload());
            pointItems.add(new PointStruct(point.id(), toFloatList(point.vector()), payload.isEmpty() ? null : payload));
        }
        UpsertRequest request = UpsertRequest.points(pointItems, true);
        String requestBody = serializeRequest(request);
        exportRequestToFile(requestBody);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Qdrant upsert request body: {}", requestBody);
        }
        try {
            LOG.debug(
                "Upserting {} points into Qdrant collection '{}' at {}",
                pointItems.size(),
                qdrantProperties.getCollection(),
                qdrantProperties.getUrl()
            );

            restTemplate.put("/collections/{collection}/points", request, qdrantProperties.getCollection());
        } catch (RestClientException e) {
            LOG.error("Failed to upsert points into Qdrant collection '{}'", qdrantProperties.getCollection(), e);
            // throw e;
        }
    }

    public void deleteByExtId(Long extId) {
        if (extId == null) {
            return;
        }
        DeleteRequest request = DeleteRequest.of(extId);
        try {
            LOG.debug(
                "Deleting Qdrant points for extId {} via {}/collections/{}/points/delete",
                extId,
                qdrantProperties.getUrl(),
                qdrantProperties.getCollection()
            );
            restTemplate.postForLocation("/collections/{collection}/points/delete", request, qdrantProperties.getCollection());
        } catch (RestClientException e) {
            LOG.error("Failed to delete points for extId {} from Qdrant", extId, e);
            throw e;
        }
    }

    public List<ScoredPoint> search(float[] vector, int limit) {
        if (vector == null || vector.length == 0) {
            return Collections.emptyList();
        }
        SearchRequest request = new SearchRequest(toFloatList(vector), limit, true, false);
        try {
            LOG.debug(
                "Searching Qdrant collection '{}' at {} with limit {}",
                qdrantProperties.getCollection(),
                qdrantProperties.getUrl(),
                limit
            );
            SearchResponse response = restTemplate.postForObject(
                "/collections/{collection}/points/search",
                request,
                SearchResponse.class,
                qdrantProperties.getCollection()
            );
            if (response == null || CollectionUtils.isEmpty(response.result())) {
                return Collections.emptyList();
            }
            return response.result();
        } catch (RestClientException e) {
            LOG.error("Failed to search Qdrant collection {}", qdrantProperties.getCollection(), e);
            throw e;
        }
    }

    private List<Float> toFloatList(float[] vector) {
        List<Float> target = new ArrayList<>(vector.length);
        for (float v : vector) {
            target.add(v);
        }
        return target;
    }

    private Map<String, Object> normalizePayload(Map<String, Object> payload) {
        if (payload == null || payload.isEmpty()) {
            return Collections.emptyMap();
        }
        return payload;
    }

    private String serializeRequest(Object request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            LOG.warn("Unable to serialize Qdrant upsert request for logging", e);
            return "<serialization-error>";
        }
    }

    private void exportRequestToFile(String requestBody) {
        Path outputPath = Path.of("d:\\logs\\reelly-data.json");
        try {
            if (requestBody == null) {
                requestBody = "";
            }
            Files.writeString(
                outputPath,
                requestBody,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
            );
        } catch (IOException e) {
            LOG.warn("Unable to export Qdrant upsert payload to {}", outputPath, e);
        }
        return;
    }

    public record QdrantPoint(String id, float[] vector, Map<String, Object> payload) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ScoredPoint(
        @JsonProperty("id") Object id,
        @JsonProperty("score") Double score,
        @JsonProperty("payload") Map<String, Object> payload
    ) {}

    private record UpsertRequest(@JsonProperty("points") List<PointStruct> points, @JsonProperty("wait") boolean waitForResult) {
        static UpsertRequest points(List<PointStruct> points, boolean waitForResult) {
            return new UpsertRequest(points, waitForResult);
        }
    }

    private record PointStruct(
        @JsonProperty("id") Object id,
        @JsonProperty("vector") List<Float> vector,
        @JsonProperty("payload") Map<String, Object> payload
    ) {}

    private record SearchRequest(
        List<Float> vector,
        int limit,
        @JsonProperty("with_payload") boolean withPayload,
        @JsonProperty("with_vectors") boolean withVectors
    ) {}

    private record SearchResponse(List<ScoredPoint> result) {}

    private record DeleteRequest(Filter filter, @JsonProperty("wait") boolean waitForResult) {
        static DeleteRequest of(Long extId) {
            return new DeleteRequest(new Filter(List.of(new FilterCondition("extId", new MatchValue(extId)))), true);
        }
    }

    private record Filter(@JsonProperty("must") List<FilterCondition> must) {}

    private record FilterCondition(String key, MatchValue match) {}

    private record MatchValue(@JsonProperty("value") Object value) {}
}
