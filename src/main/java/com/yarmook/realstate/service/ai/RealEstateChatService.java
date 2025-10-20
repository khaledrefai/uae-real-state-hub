package com.yarmook.realstate.service.ai;

import com.yarmook.realstate.config.ApplicationProperties;
import com.yarmook.realstate.service.ai.QdrantClient.ScoredPoint;
import com.yarmook.realstate.service.dto.ChatRequestDTO;
import com.yarmook.realstate.service.dto.ChatResponseDTO;
import com.yarmook.realstate.service.dto.PropertyContextDTO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class RealEstateChatService {

    private static final Logger LOG = LoggerFactory.getLogger(RealEstateChatService.class);
    private static final String SYSTEM_PROMPT =
        "You are a real-estate assistant. Answer from the provided context only. If unknown, say you don’t know.";

    private final ApplicationProperties applicationProperties;
    private final OpenAiClient openAiClient;
    private final QdrantClient qdrantClient;

    public RealEstateChatService(ApplicationProperties applicationProperties, OpenAiClient openAiClient, QdrantClient qdrantClient) {
        this.applicationProperties = applicationProperties;
        this.openAiClient = openAiClient;
        this.qdrantClient = qdrantClient;
    }

    public ChatResponseDTO chat(ChatRequestDTO request) {
        ApplicationProperties.Ai ai = applicationProperties.getAi();
        if (!ai.isEnabled()) {
            throw new IllegalStateException("AI assistant is disabled");
        }

        String userMessage = Optional.ofNullable(request.getMessage()).map(String::trim).orElse("");
        if (!StringUtils.hasText(userMessage)) {
            throw new IllegalArgumentException("Chat message must not be empty");
        }

        List<float[]> embeddings = openAiClient.embed(List.of(userMessage));
        if (CollectionUtils.isEmpty(embeddings)) {
            throw new IllegalStateException("Failed to obtain embedding for user message");
        }
        float[] queryEmbedding = embeddings.get(0);

        int limit = Optional.ofNullable(ai.getQdrant().getSearchLimit()).orElse(12);
        List<ScoredPoint> scoredPoints = qdrantClient.search(queryEmbedding, limit * 2);
        Map<Long, List<ScoredPoint>> groupedByExtId = new LinkedHashMap<>();
        for (ScoredPoint point : scoredPoints) {
            Map<String, Object> payload = point.payload();
            if (payload == null) {
                continue;
            }
            Object extIdValue = payload.get("extId");
            if (!(extIdValue instanceof Number extIdNumber)) {
                continue;
            }
            long extId = extIdNumber.longValue();
            groupedByExtId.computeIfAbsent(extId, key -> new ArrayList<>()).add(point);
        }

        if (groupedByExtId.isEmpty()) {
            return fallbackResponse();
        }

        int maxProperties = Math.min(Math.max(limit, 1), 12);
        AtomicInteger propertyCounter = new AtomicInteger();
        List<PropertyContextDTO> contextEntries = groupedByExtId
            .entrySet()
            .stream()
            .sorted(Comparator.comparingDouble(entry -> bestScore(entry.getValue())).reversed())
            .limit(maxProperties)
            .map(entry -> toPropertyContext(entry.getKey(), entry.getValue(), propertyCounter.getAndIncrement()))
            .filter(Objects::nonNull)
            .toList();

        if (contextEntries.isEmpty()) {
            return fallbackResponse();
        }

        String structuredContext = buildStructuredContext(contextEntries);
        String answer = openAiClient.chat(SYSTEM_PROMPT, structuredContext, userMessage);

        ChatResponseDTO dto = new ChatResponseDTO();
        dto.setAnswer(answer);
        dto.setContext(contextEntries);
        return dto;
    }

    private ChatResponseDTO fallbackResponse() {
        ChatResponseDTO dto = new ChatResponseDTO();
        dto.setAnswer("I don’t know.");
        dto.setContext(new ArrayList<>());
        return dto;
    }

    private double bestScore(List<ScoredPoint> points) {
        return points.stream().map(ScoredPoint::score).filter(Objects::nonNull).mapToDouble(Double::doubleValue).max().orElse(0d);
    }

    private PropertyContextDTO toPropertyContext(Long extId, List<ScoredPoint> points, int index) {
        List<ScoredPoint> sorted = points
            .stream()
            .sorted(Comparator.comparingDouble(point -> point.score() != null ? point.score() : 0d).reversed())
            .toList();
        ScoredPoint top = sorted.isEmpty() ? null : sorted.get(0);
        if (top == null) {
            return null;
        }
        Map<String, Object> payload = top.payload();
        if (payload == null) {
            return null;
        }

        PropertyContextDTO dto = new PropertyContextDTO();
        dto.setPropertyId(asLong(payload.get("propertyId")));
        dto.setExtId(extId);
        dto.setScore(top.score());
        dto.setSlug(asString(payload.get("slug")));
        dto.setName(asString(payload.get("name")));
        dto.setDeveloper(asString(payload.get("developer")));
        dto.setArea(asString(payload.get("area")));
        dto.setCity(asString(payload.get("city")));
        dto.setCountry(asString(payload.get("country")));
        dto.setStatus(asString(payload.get("status")));
        dto.setPriceRange(asString(payload.get("priceRange")));
        dto.setUnitsRange(asString(payload.get("unitsRange")));
        dto.setMinPriceAed(asDouble(payload.get("minPriceAed")));

        List<String> keyPoints = sorted
            .stream()
            .limit(3)
            .map(point -> asString(point.payload().get("chunkText")))
            .filter(StringUtils::hasText)
            .map(String::trim)
            .map(this::truncateChunk)
            .toList();
        dto.setKeyPoints(new ArrayList<>(keyPoints));
        return dto;
    }

    private String buildStructuredContext(List<PropertyContextDTO> context) {
        StringBuilder builder = new StringBuilder();
        for (PropertyContextDTO dto : context) {
            builder.append("Property #").append(dto.getExtId()).append(": ").append(optionalValue(dto.getName()));
            if (StringUtils.hasText(dto.getDeveloper())) {
                builder.append(" by ").append(dto.getDeveloper());
            }
            List<String> locationTokens = new ArrayList<>();
            if (StringUtils.hasText(dto.getArea())) {
                locationTokens.add(dto.getArea());
            }
            if (StringUtils.hasText(dto.getCity())) {
                locationTokens.add(dto.getCity());
            }
            if (StringUtils.hasText(dto.getCountry())) {
                locationTokens.add(dto.getCountry());
            }
            if (!locationTokens.isEmpty()) {
                builder.append(" (").append(String.join(", ", locationTokens)).append(")");
            }
            if (StringUtils.hasText(dto.getStatus())) {
                builder.append(" | Status: ").append(dto.getStatus());
            }
            if (StringUtils.hasText(dto.getPriceRange())) {
                builder.append(" | Pricing: ").append(dto.getPriceRange());
            }
            if (StringUtils.hasText(dto.getUnitsRange())) {
                builder.append(" | Units: ").append(dto.getUnitsRange());
            }
            builder.append("\nKey points:\n");
            if (CollectionUtils.isEmpty(dto.getKeyPoints())) {
                builder.append("- No additional details available.\n");
            } else {
                dto.getKeyPoints().forEach(point -> builder.append("- ").append(point.trim()).append("\n"));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private String optionalValue(String value) {
        return StringUtils.hasText(value) ? value : "Unknown property";
    }

    private String truncateChunk(String chunk) {
        if (!StringUtils.hasText(chunk)) {
            return "";
        }
        String normalized = chunk.replaceAll("\\s+", " ").trim();
        if (normalized.length() <= 400) {
            return normalized;
        }
        return normalized.substring(0, 400) + "...";
    }

    private String asString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String s) {
            return s;
        }
        return String.valueOf(value);
    }

    private Double asDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value instanceof String s && StringUtils.hasText(s)) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                LOG.trace("Unable to parse double from {}", s, e);
            }
        }
        return null;
    }

    private Long asLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value instanceof String s && StringUtils.hasText(s)) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                LOG.trace("Unable to parse long from {}", s, e);
            }
        }
        return null;
    }
}
