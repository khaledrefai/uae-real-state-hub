package com.yarmook.realstate.service.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yarmook.realstate.config.ApplicationProperties;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
public class OpenAiClient {

    private static final Logger LOG = LoggerFactory.getLogger(OpenAiClient.class);

    private final ApplicationProperties.Ai.OpenAi openAiProperties;
    private final RestTemplate restTemplate;

    public OpenAiClient(RestTemplateBuilder restTemplateBuilder, ApplicationProperties applicationProperties) {
        this.openAiProperties = applicationProperties.getAi().getOpenAi();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            if (StringUtils.hasText(openAiProperties.getApiKey())) {
                request.getHeaders().setBearerAuth(openAiProperties.getApiKey());
            }
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return execution.execute(request, body);
        });
        this.restTemplate = restTemplateBuilder
            .rootUri(openAiProperties.getBaseUrl())
            .setConnectTimeout(Duration.ofSeconds(30))
            .setReadTimeout(Duration.ofSeconds(60))
            .additionalInterceptors(interceptors)
            .build();
    }

    public List<float[]> embed(List<String> inputs) {
        if (CollectionUtils.isEmpty(inputs)) {
            return Collections.emptyList();
        }
        assertConfigured();
        EmbeddingRequest request = new EmbeddingRequest(openAiProperties.getEmbeddingsModel(), inputs);
        try {
            EmbeddingResponse response = restTemplate.postForObject("/embeddings", request, EmbeddingResponse.class);
            if (response == null || CollectionUtils.isEmpty(response.data())) {
                throw new IllegalStateException("OpenAI embeddings API returned an empty response");
            }
            List<float[]> vectors = new ArrayList<>(response.data().size());
            for (EmbeddingData datum : response.data()) {
                if (CollectionUtils.isEmpty(datum.embedding())) {
                    continue;
                }
                float[] vector = new float[datum.embedding().size()];
                for (int i = 0; i < datum.embedding().size(); i++) {
                    vector[i] = datum.embedding().get(i).floatValue();
                }
                vectors.add(vector);
            }
            return vectors;
        } catch (RestClientException e) {
            LOG.error("Failed to fetch embeddings from OpenAI", e);
            throw e;
        }
    }

    public String chat(String systemPrompt, String context, String question) {
        assertConfigured();
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", systemPrompt));
        messages.add(new ChatMessage("user", buildUserPrompt(context, question)));
        ChatRequest request = new ChatRequest(openAiProperties.getChatModel(), openAiProperties.getTemperature(), messages);
        try {
            ChatResponse response = restTemplate.postForObject("/chat/completions", request, ChatResponse.class);
            if (response == null || CollectionUtils.isEmpty(response.choices())) {
                throw new IllegalStateException("OpenAI chat API returned an empty response");
            }
            ChatChoice choice = response.choices().get(0);
            if (choice == null || choice.message() == null || !StringUtils.hasText(choice.message().content())) {
                throw new IllegalStateException("OpenAI chat API returned an empty message");
            }
            return choice.message().content();
        } catch (RestClientException e) {
            LOG.error("Failed to call OpenAI chat completion API", e);
            throw e;
        }
    }

    private void assertConfigured() {
        if (!StringUtils.hasText(openAiProperties.getApiKey())) {
            throw new IllegalStateException("OpenAI API key is not configured");
        }
        if (!StringUtils.hasText(openAiProperties.getBaseUrl())) {
            throw new IllegalStateException("OpenAI base URL is not configured");
        }
    }

    private String buildUserPrompt(String context, String question) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.hasText(context)) {
            builder.append("Context:\n").append(context.trim()).append("\n\n");
        }
        builder.append("Question: ").append(Objects.requireNonNullElse(question, "").trim());
        return builder.toString();
    }

    private record EmbeddingRequest(String model, List<String> input) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record EmbeddingResponse(List<EmbeddingData> data) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record EmbeddingData(List<Double> embedding) {}

    private record ChatRequest(String model, Double temperature, List<ChatMessage> messages) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ChatResponse(List<ChatChoice> choices) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ChatChoice(ChatMessage message) {}

    private record ChatMessage(@JsonProperty("role") String role, @JsonProperty("content") String content) {}
}
