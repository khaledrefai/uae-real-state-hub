package com.yarmook.realstate.service.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yarmook.realstate.config.ApplicationProperties;
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

    public QdrantClient(RestTemplateBuilder restTemplateBuilder, ApplicationProperties applicationProperties) {
        this.qdrantProperties = applicationProperties.getAi().getQdrant();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            if (StringUtils.hasText(qdrantProperties.getApiKey())) {
                request.getHeaders().set("api-key", qdrantProperties.getApiKey());
            }
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return execution.execute(request, body);
        });
        this.restTemplate = restTemplateBuilder
            .rootUri(qdrantProperties.getUrl())
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(30))
            .additionalInterceptors(interceptors)
            .build();
    }

    public void replacePoints(Long extId, List<QdrantPoint> points) {
        deleteByExtId(extId);
        upsert(points);
    }

    public void upsert(List<QdrantPoint> points) {
        if (CollectionUtils.isEmpty(points)) {
            return;
        }
        List<PointPayload> payloads = new ArrayList<>(points.size());
        for (QdrantPoint point : points) {
            payloads.add(new PointPayload(point.id(), toFloatList(point.vector()), point.payload()));
        }
        UpsertRequest request = new UpsertRequest(payloads, true);
        try {
            restTemplate.postForLocation("/collections/{collection}/points", request, qdrantProperties.getCollection());
        } catch (RestClientException e) {
            LOG.error("Failed to upsert points into Qdrant", e);
            throw e;
        }
    }

    public void deleteByExtId(Long extId) {
        if (extId == null) {
            return;
        }
        DeleteRequest request = DeleteRequest.of(extId);
        try {
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

    public record QdrantPoint(String id, float[] vector, Map<String, Object> payload) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ScoredPoint(
        @JsonProperty("id") Object id,
        @JsonProperty("score") Double score,
        @JsonProperty("payload") Map<String, Object> payload
    ) {}

    private record PointPayload(Object id, List<Float> vector, Map<String, Object> payload) {}

    private record UpsertRequest(List<PointPayload> points, @JsonProperty("wait") boolean waitForResult) {}

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
