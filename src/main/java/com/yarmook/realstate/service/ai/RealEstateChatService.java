package com.yarmook.realstate.service.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yarmook.realstate.config.ApplicationProperties;
import com.yarmook.realstate.service.ContactLeadService;
import com.yarmook.realstate.service.PropertyQueryService;
import com.yarmook.realstate.service.criteria.PropertyCriteria;
import com.yarmook.realstate.service.dto.AiAgentStateDTO;
import com.yarmook.realstate.service.dto.ChatRequestDTO;
import com.yarmook.realstate.service.dto.ChatResponseDTO;
import com.yarmook.realstate.service.dto.ContactLeadDTO;
import com.yarmook.realstate.service.dto.PropertyContextDTO;
import com.yarmook.realstate.service.dto.PropertyDTO;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Intelligent AI-powered real estate chat service.
 * Uses ChatGPT for natural language understanding and response generation,
 * and Qdrant for semantic property search (RAG - Retrieval Augmented Generation).
 */
@Service
public class RealEstateChatService {

    private static final Logger LOG = LoggerFactory.getLogger(RealEstateChatService.class);

    // System prompt for ChatGPT - defines the AI agent's personality and behavior
    private static final String SYSTEM_PROMPT =
        """
        You are an expert UAE real estate advisor assistant. Your role is to help clients find their perfect property in the UAE.

        Your conversation style:
        - Be warm, professional, and consultative
        - Ask smart qualifying questions to understand the client's needs
        - Provide insights about the UAE property market when relevant
        - Be concise but informative

        Key information to gather (ask one at a time, naturally):
        1. BUDGET: What is their approximate budget in AED? (e.g., 1M-3M AED)
        2. PURPOSE: Is this for investment, personal use, or rental income?
        3. LOCATION: Any preferred areas? (Dubai Marina, Downtown, Palm Jumeirah, etc.)
        4. TIMELINE: When do they need the property? (Ready now, off-plan 2025/2026?)
        5. TYPE: Apartment, villa, townhouse, or penthouse?

        When you have property data in the context:
        - Highlight key selling points relevant to their stated needs
        - For investors: mention ROI potential, rental yields, capital appreciation areas
        - For end-users: mention lifestyle, amenities, family-friendliness
        - Always mention developer reputation when relevant

        Response format:
        - Keep responses under 200 words unless explaining properties
        - Use bullet points for property listings
        - Ask a follow-up question to keep the conversation going

        If the user seems ready to proceed, offer to connect them with an advisor.

        IMPORTANT: You MUST respond ONLY in valid JSON format with this structure:
        {
            "message": "Your conversational response here",
            "extractedInfo": {
                "budget": null or number in AED,
                "purpose": null or "INVESTMENT" or "END_USE" or "RENTAL",
                "location": null or "area name",
                "propertyType": null or "APARTMENT" or "VILLA" or "TOWNHOUSE" or "PENTHOUSE",
                "timeline": null or "READY" or year like 2025,
                "readyForAdvisor": false or true
            },
            "searchQuery": "semantic search query for properties based on conversation" or null
        }
        """;

    private static final String AMOUNT_REGEX =
        "(?:(?:aed|dhs?|dirhams?)\\s*)?\\d+(?:[\\.,]\\d+)?(?:\\s*(?:k|m|mn|b|bn|million|thousand|billion))?(?:\\s*(?:aed|dhs?|dirhams?))?";
    private static final Pattern MAX_PATTERN = Pattern.compile(
        String.format(
            "(?i)(?:under|less than|below|up to|no more than|maximum(?:\\s+of)?|budget(?:\\s+(?:is|=|of))?|cap(?:\\s+of)?|cheaper than)\\s+(?<amount>%s)",
            AMOUNT_REGEX
        )
    );
    private static final Pattern AMOUNT_VALUE_PATTERN = Pattern.compile(
        "(?i)(?<value>\\d+(?:[\\.,]\\d+)?)(?:\\s*(?<unit>k|m|mn|b|bn|million|thousand|billion))?"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile("(?:(?:\\+|00)\\d{1,3}[\\s-]?)?\\d[\\d\\s-]{6,}");
    private static final Pattern ANY_AMOUNT_PATTERN = Pattern.compile("(?i)" + AMOUNT_REGEX);

    // Known UAE locations for extraction
    private static final Set<String> KNOWN_CITIES = Set.of(
        "dubai",
        "abu dhabi",
        "sharjah",
        "ajman",
        "ras al khaimah",
        "fujairah",
        "umm al quwain"
    );
    private static final Set<String> KNOWN_AREAS = Set.of(
        "dubai marina",
        "downtown dubai",
        "palm jumeirah",
        "business bay",
        "jumeirah beach residence",
        "jbr",
        "dubai hills",
        "dubai creek harbour",
        "dubai south",
        "arabian ranches",
        "emaar beachfront",
        "dubai harbour",
        "city walk",
        "meydan",
        "al barsha",
        "deira",
        "bur dubai",
        "jumeirah",
        "jumeirah village circle",
        "jvc",
        "jumeirah lake towers",
        "jlt",
        "motor city",
        "sports city",
        "dubai silicon oasis",
        "international city",
        "al quoz",
        "production city",
        "studio city",
        "damac hills",
        "tilal al ghaf",
        "sobha hartland",
        "mohammed bin rashid city",
        "mbr city",
        "al reem island",
        "yas island",
        "saadiyat island",
        "al maryah island",
        "masdar city"
    );

    private final ApplicationProperties applicationProperties;
    private final PropertyQueryService propertyQueryService;
    private final ContactLeadService contactLeadService;
    private final OpenAiClient openAiClient;
    private final QdrantClient qdrantClient;
    private final ObjectMapper objectMapper;

    public RealEstateChatService(
        ApplicationProperties applicationProperties,
        PropertyQueryService propertyQueryService,
        ContactLeadService contactLeadService,
        OpenAiClient openAiClient,
        QdrantClient qdrantClient,
        ObjectMapper objectMapper
    ) {
        this.applicationProperties = applicationProperties;
        this.propertyQueryService = propertyQueryService;
        this.contactLeadService = contactLeadService;
        this.openAiClient = openAiClient;
        this.qdrantClient = qdrantClient;
        this.objectMapper = objectMapper;
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

        LOG.info("Processing chat message: {}", userMessage);
        AgentState state = AgentState.from(request.getAgentState());

        try {
            ConversationStepResult result = processWithAI(state, userMessage);
            LOG.info("AI response generated, properties found: {}", result.context().size());

            ChatResponseDTO responseDTO = new ChatResponseDTO();
            responseDTO.setAnswer(result.reply());
            responseDTO.setContext(result.context());
            responseDTO.setAgentState(result.state().toDto());
            responseDTO.setLeadCreated(result.leadCreated());
            return responseDTO;
        } catch (Exception e) {
            LOG.error("Error processing chat with AI, falling back to rule-based", e);
            return fallbackToRuleBased(state, userMessage);
        }
    }

    /**
     * Process the conversation using ChatGPT and RAG.
     */
    private ConversationStepResult processWithAI(AgentState state, String userMessage) {
        // Step 1: Build conversation context for ChatGPT
        String conversationContext = buildConversationContext(state, userMessage);

        // Step 2: Perform semantic search using RAG if we have enough context
        List<PropertyContextDTO> ragResults = performSemanticSearch(state, userMessage);

        // Step 3: Build property context for ChatGPT
        String propertyContext = buildPropertyContext(ragResults);

        // Step 4: Call ChatGPT for intelligent response
        String fullContext = conversationContext + "\n\n" + propertyContext;
        String aiResponse = callChatGPT(fullContext, userMessage);

        // Step 5: Parse AI response and extract structured data
        AiResponseData parsedResponse = parseAiResponse(aiResponse, state);

        // Step 6: Update state with extracted information
        updateStateFromAI(state, parsedResponse);

        // Step 7: If AI suggests more properties, search again with refined criteria
        if (StringUtils.hasText(parsedResponse.searchQuery) && ragResults.isEmpty()) {
            ragResults = performSemanticSearchWithQuery(parsedResponse.searchQuery);
        }

        // Step 8: Handle lead capture if user is ready
        if (parsedResponse.readyForAdvisor && state.isInterestConfirmed()) {
            return handleLeadCapture(state, ragResults, userMessage, parsedResponse.message);
        }

        // Step 9: Combine database results with RAG results for comprehensive coverage
        List<PropertyContextDTO> combinedResults = combineResults(ragResults, state);

        return ConversationStepResult.of(parsedResponse.message, combinedResults, state, false);
    }

    private String buildConversationContext(AgentState state, String userMessage) {
        StringBuilder context = new StringBuilder();
        context.append("Current conversation state:\n");

        if (state.hasBudget()) {
            context.append("- Budget: ").append(formatAed(state.getBudgetMaxAed())).append("\n");
        }
        if (state.hasPurpose()) {
            context.append("- Purpose: ").append(state.getPurpose()).append("\n");
        }
        if (StringUtils.hasText(state.getCity())) {
            context.append("- Preferred City: ").append(state.getCity()).append("\n");
        }
        if (StringUtils.hasText(state.getArea())) {
            context.append("- Preferred Area: ").append(state.getArea()).append("\n");
        }
        if (state.getCompletionYearTo() != null) {
            context.append("- Timeline: Ready by ").append(state.getCompletionYearTo()).append("\n");
        }
        if (state.hasLeadName()) {
            context.append("- Client Name: ").append(state.getLeadName()).append("\n");
        }

        context.append("\nConversation stage: ").append(state.getStage().name());

        return context.toString();
    }

    private String buildPropertyContext(List<PropertyContextDTO> properties) {
        if (CollectionUtils.isEmpty(properties)) {
            return "No properties found matching current criteria yet.";
        }

        StringBuilder context = new StringBuilder();
        context.append("Available properties matching criteria:\n\n");

        for (int i = 0; i < Math.min(properties.size(), 5); i++) {
            PropertyContextDTO prop = properties.get(i);
            context.append(i + 1).append(". ").append(prop.getName() != null ? prop.getName() : "Property");
            if (StringUtils.hasText(prop.getArea()) || StringUtils.hasText(prop.getCity())) {
                context
                    .append(" (")
                    .append(String.join(", ", Arrays.asList(prop.getArea(), prop.getCity()).stream().filter(StringUtils::hasText).toList()))
                    .append(")");
            }
            context.append("\n");
            if (StringUtils.hasText(prop.getDeveloper())) {
                context.append("   Developer: ").append(prop.getDeveloper()).append("\n");
            }
            if (StringUtils.hasText(prop.getPriceRange())) {
                context.append("   Price: ").append(prop.getPriceRange()).append("\n");
            }
            if (StringUtils.hasText(prop.getStatus())) {
                context.append("   Status: ").append(prop.getStatus()).append("\n");
            }
            if (!CollectionUtils.isEmpty(prop.getKeyPoints())) {
                context.append("   Key points: ").append(String.join(", ", prop.getKeyPoints())).append("\n");
            }
            context.append("\n");
        }

        return context.toString();
    }

    private String callChatGPT(String context, String userMessage) {
        try {
            return openAiClient.chat(SYSTEM_PROMPT, context, userMessage);
        } catch (Exception e) {
            LOG.error("ChatGPT call failed", e);
            throw new RuntimeException("Failed to get AI response", e);
        }
    }

    private AiResponseData parseAiResponse(String aiResponse, AgentState state) {
        try {
            // Try to parse as JSON
            JsonNode root = objectMapper.readTree(aiResponse);

            String message = root.has("message") ? root.get("message").asText() : aiResponse;
            String searchQuery = root.has("searchQuery") && !root.get("searchQuery").isNull() ? root.get("searchQuery").asText() : null;
            boolean readyForAdvisor =
                root.has("extractedInfo") &&
                root.get("extractedInfo").has("readyForAdvisor") &&
                root.get("extractedInfo").get("readyForAdvisor").asBoolean();

            AiResponseData data = new AiResponseData();
            data.message = message;
            data.searchQuery = searchQuery;
            data.readyForAdvisor = readyForAdvisor;

            // Extract structured info
            if (root.has("extractedInfo")) {
                JsonNode info = root.get("extractedInfo");
                if (info.has("budget") && !info.get("budget").isNull()) {
                    data.budget = info.get("budget").asDouble();
                }
                if (info.has("purpose") && !info.get("purpose").isNull()) {
                    data.purpose = info.get("purpose").asText();
                }
                if (info.has("location") && !info.get("location").isNull()) {
                    data.location = info.get("location").asText();
                }
                if (info.has("propertyType") && !info.get("propertyType").isNull()) {
                    data.propertyType = info.get("propertyType").asText();
                }
            }

            return data;
        } catch (JsonProcessingException e) {
            // If not valid JSON, treat the whole response as the message
            LOG.warn("AI response not in JSON format, using as plain text");
            AiResponseData data = new AiResponseData();
            data.message = aiResponse;
            return data;
        }
    }

    private void updateStateFromAI(AgentState state, AiResponseData aiData) {
        if (aiData.budget != null && aiData.budget > 0) {
            state.setBudgetMaxAed(aiData.budget);
        }
        if (StringUtils.hasText(aiData.purpose)) {
            state.setPurpose(aiData.purpose);
        }
        if (StringUtils.hasText(aiData.location)) {
            // Check if it's a city or area
            String lower = aiData.location.toLowerCase(Locale.ENGLISH);
            if (KNOWN_CITIES.contains(lower)) {
                state.setCity(capitalize(aiData.location));
            } else {
                state.setArea(capitalize(aiData.location));
            }
        }
        if (aiData.readyForAdvisor) {
            state.setInterestConfirmed(true);
        }
    }

    /**
     * Perform semantic search using Qdrant vector database (RAG).
     */
    private List<PropertyContextDTO> performSemanticSearch(AgentState state, String userMessage) {
        try {
            // Build search query based on state and user message
            String searchQuery = buildSemanticSearchQuery(state, userMessage);
            return performSemanticSearchWithQuery(searchQuery);
        } catch (Exception e) {
            LOG.error("Semantic search failed", e);
            return Collections.emptyList();
        }
    }

    private List<PropertyContextDTO> performSemanticSearchWithQuery(String searchQuery) {
        if (!StringUtils.hasText(searchQuery)) {
            return Collections.emptyList();
        }

        try {
            // Get embedding for the search query
            List<float[]> embeddings = openAiClient.embed(List.of(searchQuery));
            if (CollectionUtils.isEmpty(embeddings)) {
                return Collections.emptyList();
            }

            float[] queryVector = embeddings.get(0);

            // Search Qdrant
            int searchLimit = applicationProperties.getAi().getQdrant().getSearchLimit();
            List<QdrantClient.ScoredPoint> results = qdrantClient.search(queryVector, searchLimit);

            LOG.info("Semantic search found {} results for query: {}", results.size(), searchQuery);

            // Convert Qdrant results to PropertyContextDTO
            return results.stream().map(this::scoredPointToPropertyContext).filter(Objects::nonNull).toList();
        } catch (Exception e) {
            LOG.error("Error in semantic search", e);
            return Collections.emptyList();
        }
    }

    private String buildSemanticSearchQuery(AgentState state, String userMessage) {
        StringBuilder query = new StringBuilder();
        query.append("UAE property ");

        if (state.hasBudget()) {
            query.append("under ").append(formatAed(state.getBudgetMaxAed())).append(" ");
        }
        if (StringUtils.hasText(state.getArea())) {
            query.append("in ").append(state.getArea()).append(" ");
        } else if (StringUtils.hasText(state.getCity())) {
            query.append("in ").append(state.getCity()).append(" ");
        }
        if (state.hasPurpose()) {
            if ("INVESTMENT".equals(state.getPurpose()) || "INVEST".equals(state.getPurpose())) {
                query.append("investment property high ROI ");
            } else if ("END_USE".equals(state.getPurpose())) {
                query.append("family living residential ");
            } else if ("RENTAL".equals(state.getPurpose())) {
                query.append("rental income yield ");
            }
        }

        // Add user message for additional context
        query.append(userMessage);

        return query.toString().trim();
    }

    private PropertyContextDTO scoredPointToPropertyContext(QdrantClient.ScoredPoint point) {
        if (point == null || point.payload() == null) {
            return null;
        }

        Map<String, Object> payload = point.payload();
        PropertyContextDTO dto = new PropertyContextDTO();

        dto.setExtId(getLongFromPayload(payload, "extId"));
        dto.setName(getStringFromPayload(payload, "name"));
        dto.setDeveloper(getStringFromPayload(payload, "developer"));
        dto.setArea(getStringFromPayload(payload, "area"));
        dto.setCity(getStringFromPayload(payload, "city"));
        dto.setCountry(getStringFromPayload(payload, "country"));
        dto.setStatus(getStringFromPayload(payload, "status"));
        dto.setPriceRange(getStringFromPayload(payload, "priceRange"));
        dto.setScore(point.score());

        // Extract key points from chunk text if available
        String chunkText = getStringFromPayload(payload, "chunkText");
        if (StringUtils.hasText(chunkText)) {
            List<String> keyPoints = new ArrayList<>();
            keyPoints.add(chunkText.length() > 150 ? chunkText.substring(0, 150) + "..." : chunkText);
            dto.setKeyPoints(keyPoints);
        }

        return dto;
    }

    private String getStringFromPayload(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        return value != null ? value.toString() : null;
    }

    private Long getLongFromPayload(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private List<PropertyContextDTO> combineResults(List<PropertyContextDTO> ragResults, AgentState state) {
        // Start with RAG results
        List<PropertyContextDTO> combined = new ArrayList<>(ragResults);

        // If RAG didn't find enough, supplement with database query
        if (combined.size() < 5) {
            List<PropertyContextDTO> dbResults = findMatchingProperties(state);
            for (PropertyContextDTO dbResult : dbResults) {
                boolean alreadyExists = combined.stream().anyMatch(r -> Objects.equals(r.getExtId(), dbResult.getExtId()));
                if (!alreadyExists) {
                    combined.add(dbResult);
                }
                if (combined.size() >= 5) break;
            }
        }

        return combined.stream().limit(5).toList();
    }

    /**
     * Fallback to rule-based processing if AI fails.
     */
    private ChatResponseDTO fallbackToRuleBased(AgentState state, String userMessage) {
        ConversationStepResult result = driveConversationRuleBased(state, userMessage);

        ChatResponseDTO responseDTO = new ChatResponseDTO();
        responseDTO.setAnswer(result.reply());
        responseDTO.setContext(result.context());
        responseDTO.setAgentState(result.state().toDto());
        responseDTO.setLeadCreated(result.leadCreated());
        return responseDTO;
    }

    private ConversationStepResult driveConversationRuleBased(AgentState state, String userMessage) {
        // Extract information from user message
        extractInformationFromMessage(userMessage, state);

        // Find matching properties
        List<PropertyContextDTO> contextEntries = findMatchingProperties(state);

        // Build response based on state
        String reply = buildRuleBasedResponse(state, contextEntries, userMessage);

        return ConversationStepResult.of(reply, contextEntries, state, false);
    }

    private void extractInformationFromMessage(String userMessage, AgentState state) {
        // Extract budget
        Double budget = extractBudget(userMessage);
        if (budget != null) {
            state.setBudgetMaxAed(budget);
        }

        // Extract purpose
        String purpose = extractPurpose(userMessage);
        if (StringUtils.hasText(purpose)) {
            state.setPurpose(purpose);
        }

        // Extract location
        extractLocation(userMessage, state);

        // Extract completion timeline
        extractCompletionWindow(userMessage, state);
    }

    private String buildRuleBasedResponse(AgentState state, List<PropertyContextDTO> properties, String userMessage) {
        StringBuilder response = new StringBuilder();

        // If we found properties, show them
        if (!CollectionUtils.isEmpty(properties)) {
            if (state.hasBudget()) {
                response.append("Here are some options within your budget of ").append(formatAed(state.getBudgetMaxAed())).append(":\n\n");
            } else {
                response.append("Here are some popular properties:\n\n");
            }

            for (PropertyContextDTO prop : properties) {
                response.append("• **").append(prop.getName() != null ? prop.getName() : "Property").append("**");
                if (StringUtils.hasText(prop.getArea()) || StringUtils.hasText(prop.getCity())) {
                    response
                        .append(" (")
                        .append(
                            String.join(", ", Arrays.asList(prop.getArea(), prop.getCity()).stream().filter(StringUtils::hasText).toList())
                        )
                        .append(")");
                }
                if (StringUtils.hasText(prop.getPriceRange())) {
                    response.append(" — ").append(prop.getPriceRange());
                }
                response.append("\n");
            }
            response.append("\n");
        }

        // Ask qualifying questions based on what's missing
        if (!state.hasBudget()) {
            response.append("To help you find the perfect property, what's your approximate budget in AED?");
        } else if (!state.hasPurpose()) {
            response.append("Great! Are you looking to buy for investment, rental income, or personal use?");
        } else if (!StringUtils.hasText(state.getCity()) && !StringUtils.hasText(state.getArea())) {
            response.append("Any preferred locations? Popular choices include Dubai Marina, Downtown Dubai, or Palm Jumeirah.");
        } else if (!state.isInterestConfirmed()) {
            response.append("Would you like me to connect you with one of our property advisors for more details?");
        }

        return response.toString();
    }

    private ConversationStepResult handleLeadCapture(
        AgentState state,
        List<PropertyContextDTO> contextEntries,
        String userMessage,
        String aiMessage
    ) {
        String name = extractName(userMessage);
        if (StringUtils.hasText(name)) {
            state.setLeadName(name);
        }

        String phone = extractPhone(userMessage);
        if (StringUtils.hasText(phone)) {
            state.setLeadPhone(phone);
        }

        if (!state.hasLeadName()) {
            String message = StringUtils.hasText(aiMessage)
                ? aiMessage
                : "Perfect! What's your full name so I can connect you with an advisor?";
            return ConversationStepResult.of(message, contextEntries, state, false);
        }

        if (!state.hasLeadPhone()) {
            return ConversationStepResult.of(
                "Thanks " + state.getLeadName() + "! What's the best phone or WhatsApp number to reach you?",
                contextEntries,
                state,
                false
            );
        }

        if (!state.isLeadCaptured()) {
            persistLead(state, contextEntries);
            state.setLeadCaptured(true);
            return ConversationStepResult.of(
                "Excellent! I've shared your details with our advisory team. They'll reach out within 24 hours with personalized recommendations. Is there anything else you'd like to know about UAE real estate?",
                contextEntries,
                state,
                true
            );
        }

        return ConversationStepResult.of(
            "Your interest has been logged. Our team will contact you soon. Feel free to ask about other properties!",
            contextEntries,
            state,
            false
        );
    }

    private List<PropertyContextDTO> findMatchingProperties(AgentState state) {
        PropertyCriteria criteria = new PropertyCriteria();

        if (state.getBudgetMaxAed() != null) {
            BigDecimalFilter priceFilter = criteria.minPriceAed();
            priceFilter.setLessThanOrEqual(BigDecimal.valueOf(state.getBudgetMaxAed()));
        }

        if (StringUtils.hasText(state.getCity())) {
            StringFilter cityFilter = criteria.city();
            cityFilter.setContains(state.getCity());
        }
        if (StringUtils.hasText(state.getArea())) {
            StringFilter areaFilter = criteria.area();
            areaFilter.setContains(state.getArea());
        }

        applyCommonFilters(criteria, state);

        Pageable pageable = PageRequest.of(0, 10);
        Page<PropertyDTO> page = propertyQueryService.findByCriteria(criteria, pageable);

        List<PropertyContextDTO> contexts = page.getContent().stream().map(this::toPropertyContext).toList();

        Double budgetMax = state.getBudgetMaxAed();
        if (budgetMax == null) {
            return contexts.stream().limit(5).toList();
        }

        List<PropertyContextDTO> filtered = contexts.stream().filter(dto -> priceWithinBudget(dto, budgetMax)).limit(5).toList();

        if (!filtered.isEmpty()) {
            return filtered;
        }

        return fallbackByMinPrice(state, budgetMax);
    }

    private void extractLocation(String message, AgentState state) {
        if (!StringUtils.hasText(message)) {
            return;
        }
        String lower = message.toLowerCase(Locale.ENGLISH);

        for (String area : KNOWN_AREAS) {
            if (lower.contains(area)) {
                state.setArea(capitalize(area));
                return;
            }
        }

        for (String city : KNOWN_CITIES) {
            if (lower.contains(city)) {
                state.setCity(capitalize(city));
                return;
            }
        }
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Arrays.stream(text.split("\\s+"))
            .map(word -> word.substring(0, 1).toUpperCase(Locale.ENGLISH) + word.substring(1))
            .collect(Collectors.joining(" "));
    }

    private PropertyContextDTO toPropertyContext(PropertyDTO property) {
        PropertyContextDTO dto = new PropertyContextDTO();
        dto.setPropertyId(property.getId());
        dto.setExtId(property.getExtId());
        dto.setSlug(property.getSlug());
        dto.setName(property.getName());
        dto.setDeveloper(property.getDeveloper());
        dto.setArea(property.getArea());
        dto.setCity(property.getCity());
        dto.setCountry(property.getCountry());
        dto.setStatus(property.getStatus() != null ? property.getStatus().name() : property.getSaleStatus());
        dto.setMinPriceAed(property.getMinPriceAed() != null ? property.getMinPriceAed().doubleValue() : null);
        dto.setPriceRange(buildPriceLabel(property.getMinPriceAed(), property.getMaxPrice(), property.getPriceCurrency()));
        dto.setUnitsRange(buildAreaLabel(property.getMinArea(), property.getMaxArea(), property.getAreaUnit()));

        List<String> keyPoints = new ArrayList<>();
        if (StringUtils.hasText(property.getSaleStatus())) {
            keyPoints.add("Status: " + property.getSaleStatus());
        }
        if (property.getCompletionDateTime() != null) {
            keyPoints.add("Completion: " + property.getCompletionDateTime());
        }
        if (StringUtils.hasText(property.getDeveloper())) {
            keyPoints.add("By " + property.getDeveloper());
        }
        dto.setKeyPoints(keyPoints);
        return dto;
    }

    private void persistLead(AgentState state, List<PropertyContextDTO> contextEntries) {
        try {
            ContactLeadDTO leadDTO = new ContactLeadDTO();
            leadDTO.setName(state.getLeadName());
            leadDTO.setPhone(state.getLeadPhone());
            leadDTO.setSource("AI_AGENT");
            leadDTO.setMessage(buildLeadSummary(state, contextEntries));
            contactLeadService.save(leadDTO);
            LOG.info("Lead created successfully for: {}", state.getLeadName());
        } catch (Exception ex) {
            LOG.error("Failed to create lead from AI agent", ex);
        }
    }

    private String buildLeadSummary(AgentState state, List<PropertyContextDTO> contextEntries) {
        StringBuilder builder = new StringBuilder();
        builder.append("AI Agent Lead - ");
        builder.append("Budget: ").append(formatAed(state.getBudgetMaxAed())).append(" | ");
        builder.append("Purpose: ").append(Optional.ofNullable(state.getPurpose()).orElse("Not specified")).append(" | ");
        if (StringUtils.hasText(state.getArea())) {
            builder.append("Area: ").append(state.getArea()).append(" | ");
        }
        if (StringUtils.hasText(state.getCity())) {
            builder.append("City: ").append(state.getCity()).append(" | ");
        }
        if (!CollectionUtils.isEmpty(contextEntries)) {
            builder.append("Properties: ");
            builder.append(
                contextEntries.stream().map(PropertyContextDTO::getName).filter(Objects::nonNull).limit(3).collect(Collectors.joining(", "))
            );
        }
        return builder.toString();
    }

    private void applyCommonFilters(PropertyCriteria criteria, AgentState state) {
        if (state.getCompletionYearTo() != null || state.getCompletionYearFrom() != null) {
            InstantFilter completionFilter = criteria.completionDateTime();
            if (state.getCompletionYearTo() != null) {
                completionFilter.setLessThanOrEqual(endOfYear(state.getCompletionYearTo()));
            }
            if (state.getCompletionYearFrom() != null) {
                completionFilter.setGreaterThanOrEqual(startOfYear(state.getCompletionYearFrom()));
            }
        }
        if (state.getMinArea() != null) {
            DoubleFilter minAreaFilter = criteria.minArea();
            minAreaFilter.setGreaterThanOrEqual(state.getMinArea());
        }
        if (state.getMaxArea() != null) {
            DoubleFilter maxAreaFilter = criteria.maxArea();
            maxAreaFilter.setLessThanOrEqual(state.getMaxArea());
        }
    }

    private List<PropertyContextDTO> fallbackByMinPrice(AgentState state, Double budgetMax) {
        PropertyCriteria criteria = new PropertyCriteria();
        if (budgetMax != null) {
            BigDecimalFilter priceFilter = criteria.minPriceAed();
            priceFilter.setLessThanOrEqual(BigDecimal.valueOf(budgetMax));
        }
        Page<PropertyDTO> page = propertyQueryService.findByCriteria(criteria, PageRequest.of(0, 10));
        return page.getContent().stream().map(this::toPropertyContext).filter(dto -> priceWithinBudget(dto, budgetMax)).limit(5).toList();
    }

    private Double extractBudget(String message) {
        if (!StringUtils.hasText(message)) {
            return null;
        }
        Matcher maxMatcher = MAX_PATTERN.matcher(message);
        if (maxMatcher.find()) {
            return parseAmountToken(maxMatcher.group("amount"));
        }
        Matcher anyMatcher = ANY_AMOUNT_PATTERN.matcher(message);
        if (anyMatcher.find()) {
            return parseAmountToken(anyMatcher.group());
        }
        return null;
    }

    private String extractPurpose(String message) {
        if (!StringUtils.hasText(message)) {
            return null;
        }
        String lower = message.toLowerCase(Locale.ENGLISH);
        if (lower.contains("invest")) {
            return "INVESTMENT";
        }
        if (lower.contains("rent") || lower.contains("rental")) {
            return "RENTAL";
        }
        if (lower.contains("live") || lower.contains("personal") || lower.contains("family") || lower.contains("end use")) {
            return "END_USE";
        }
        return null;
    }

    private void extractCompletionWindow(String message, AgentState state) {
        if (!StringUtils.hasText(message)) {
            return;
        }
        Pattern pattern = Pattern.compile("(?i)(before|by|after|from|in|ready)\\s*(20\\d{2})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            int year = Integer.parseInt(matcher.group(2));
            String keyword = matcher.group(1).toLowerCase(Locale.ENGLISH);
            if (keyword.startsWith("before") || keyword.equals("by")) {
                state.setCompletionYearTo(year);
            } else if (keyword.startsWith("after") || keyword.equals("from")) {
                state.setCompletionYearFrom(year);
            } else if (keyword.equals("in") || keyword.equals("ready")) {
                state.setCompletionYearFrom(year);
                state.setCompletionYearTo(year);
            }
        }
    }

    private String extractName(String message) {
        if (!StringUtils.hasText(message)) {
            return null;
        }
        String sanitized = message.replaceAll("[^a-zA-Z\\s]", " ").trim();
        String[] words = sanitized.split("\\s+");
        if (words.length >= 2 && words.length <= 4) {
            return sanitized;
        }
        return null;
    }

    private String extractPhone(String message) {
        if (!StringUtils.hasText(message)) {
            return null;
        }
        Matcher matcher = PHONE_PATTERN.matcher(message);
        if (matcher.find()) {
            return matcher.group().replaceAll("\\s+", "");
        }
        return null;
    }

    private Instant startOfYear(int year) {
        return LocalDate.of(year, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC);
    }

    private Instant endOfYear(int year) {
        return LocalDate.of(year, 12, 31).atTime(23, 59).toInstant(ZoneOffset.UTC);
    }

    private String buildAreaLabel(Double minArea, Double maxArea, String unit) {
        if (minArea == null && maxArea == null) {
            return null;
        }
        String labelUnit = StringUtils.hasText(unit) ? unit : "sqft";
        if (minArea != null && maxArea != null) {
            if (Objects.equals(Math.round(minArea), Math.round(maxArea))) {
                return Math.round(minArea) + " " + labelUnit;
            }
            return Math.round(minArea) + "-" + Math.round(maxArea) + " " + labelUnit;
        }
        double value = minArea != null ? minArea : maxArea;
        return Math.round(value) + " " + labelUnit;
    }

    private String buildPriceLabel(BigDecimal minPrice, BigDecimal maxPrice, String currency) {
        if (minPrice == null && maxPrice == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        if (minPrice != null) {
            builder.append("from ").append(formatAed(minPrice.doubleValue(), currency));
        }
        if (maxPrice != null && (minPrice == null || maxPrice.compareTo(minPrice) != 0)) {
            if (builder.length() > 0) {
                builder.append(" to ");
            }
            builder.append(formatAed(maxPrice.doubleValue(), currency));
        }
        if (builder.length() == 0) {
            builder.append(formatAed(maxPrice != null ? maxPrice.doubleValue() : minPrice.doubleValue(), currency));
        }
        return builder.toString();
    }

    private String formatAed(Double amount) {
        return formatAed(amount, "AED");
    }

    private boolean priceWithinBudget(PropertyContextDTO dto, Double budgetMax) {
        if (budgetMax == null || dto == null) {
            return true;
        }
        Double price = dto.getMinPriceAed();
        if (price == null) {
            return true;
        }
        return price <= budgetMax * 1.05; // Allow 5% flexibility
    }

    private String formatAed(Double amount, String currency) {
        if (amount == null) {
            return "AED ?";
        }
        String label = StringUtils.hasText(currency) ? currency.toUpperCase(Locale.ENGLISH) : "AED";
        double abs = Math.abs(amount);
        if (abs >= 1_000_000_000d) {
            return String.format(Locale.ENGLISH, "%s %.2fB", label, amount / 1_000_000_000d);
        }
        if (abs >= 1_000_000d) {
            return String.format(Locale.ENGLISH, "%s %.2fM", label, amount / 1_000_000d);
        }
        if (abs >= 1_000d) {
            return String.format(Locale.ENGLISH, "%s %.1fK", label, amount / 1_000d);
        }
        return String.format(Locale.ENGLISH, "%s %.0f", label, amount);
    }

    private Double parseAmountToken(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        token = token.toLowerCase(Locale.ENGLISH).replaceAll("(aed|dhs?|dirhams?)", "").trim();
        Matcher matcher = AMOUNT_VALUE_PATTERN.matcher(token);
        if (!matcher.find()) {
            return null;
        }
        String valueToken = matcher.group("value");
        if (!StringUtils.hasText(valueToken)) {
            return null;
        }
        double value = Double.parseDouble(valueToken.replace(",", ""));
        String unit = matcher.group("unit");
        double multiplier = 1d;
        if (StringUtils.hasText(unit)) {
            switch (unit.trim()) {
                case "k", "thousand" -> multiplier = 1_000d;
                case "m", "mn", "million" -> multiplier = 1_000_000d;
                case "b", "bn", "billion" -> multiplier = 1_000_000_000d;
            }
        }
        return value * multiplier;
    }

    // Helper class for parsed AI response
    private static class AiResponseData {

        String message;
        String searchQuery;
        boolean readyForAdvisor;
        Double budget;
        String purpose;
        String location;
        String propertyType;
    }

    private record ConversationStepResult(String reply, List<PropertyContextDTO> context, AgentState state, boolean leadCreated) {
        static ConversationStepResult of(String reply, List<PropertyContextDTO> context, AgentState state, boolean leadCreated) {
            state.refreshStage();
            return new ConversationStepResult(reply, context, state, leadCreated);
        }
    }

    private static final class AgentState {

        private Double budgetMaxAed;
        private String purpose;
        private String plan;
        private boolean interestConfirmed;
        private String leadName;
        private String leadPhone;
        private boolean leadCaptured;
        private Stage stage;
        private Double minArea;
        private Double maxArea;
        private Integer completionYearFrom;
        private Integer completionYearTo;
        private String city;
        private String area;

        static AgentState from(AiAgentStateDTO dto) {
            AgentState state = new AgentState();
            if (dto != null) {
                state.budgetMaxAed = dto.getBudgetMaxAed();
                state.purpose = dto.getPurpose();
                state.plan = dto.getPlan();
                state.interestConfirmed = Boolean.TRUE.equals(dto.getInterestConfirmed());
                state.leadName = dto.getLeadName();
                state.leadPhone = dto.getLeadPhone();
                state.leadCaptured = dto.isLeadCaptured();
                state.minArea = dto.getMinArea();
                state.maxArea = dto.getMaxArea();
                state.completionYearFrom = dto.getCompletionYearFrom();
                state.completionYearTo = dto.getCompletionYearTo();
                state.city = dto.getCity();
                state.area = dto.getArea();
            }
            state.refreshStage();
            return state;
        }

        boolean hasBudget() {
            return budgetMaxAed != null && budgetMaxAed > 0;
        }

        boolean hasPurpose() {
            return StringUtils.hasText(purpose);
        }

        boolean hasPlan() {
            return StringUtils.hasText(plan);
        }

        boolean isInterestConfirmed() {
            return interestConfirmed;
        }

        boolean hasLeadName() {
            return StringUtils.hasText(leadName);
        }

        boolean hasLeadPhone() {
            return StringUtils.hasText(leadPhone);
        }

        String getLeadName() {
            return leadName;
        }

        String getLeadPhone() {
            return leadPhone;
        }

        boolean isLeadCaptured() {
            return leadCaptured;
        }

        Double getBudgetMaxAed() {
            return budgetMaxAed;
        }

        void setBudgetMaxAed(Double budgetMaxAed) {
            this.budgetMaxAed = budgetMaxAed;
        }

        String getPurpose() {
            return purpose;
        }

        void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        String getPlan() {
            return plan;
        }

        void setPlan(String plan) {
            this.plan = plan;
        }

        void setInterestConfirmed(boolean interestConfirmed) {
            this.interestConfirmed = interestConfirmed;
        }

        void setLeadName(String leadName) {
            this.leadName = leadName;
        }

        void setLeadPhone(String leadPhone) {
            this.leadPhone = leadPhone;
        }

        void setLeadCaptured(boolean leadCaptured) {
            this.leadCaptured = leadCaptured;
        }

        Double getMinArea() {
            return minArea;
        }

        void setMinArea(Double minArea) {
            this.minArea = minArea;
        }

        Double getMaxArea() {
            return maxArea;
        }

        void setMaxArea(Double maxArea) {
            this.maxArea = maxArea;
        }

        Integer getCompletionYearFrom() {
            return completionYearFrom;
        }

        void setCompletionYearFrom(Integer completionYearFrom) {
            this.completionYearFrom = completionYearFrom;
        }

        Integer getCompletionYearTo() {
            return completionYearTo;
        }

        void setCompletionYearTo(Integer completionYearTo) {
            this.completionYearTo = completionYearTo;
        }

        String getCity() {
            return city;
        }

        void setCity(String city) {
            this.city = city;
        }

        String getArea() {
            return area;
        }

        void setArea(String area) {
            this.area = area;
        }

        Stage getStage() {
            return stage != null ? stage : Stage.COLLECT_BUDGET;
        }

        void refreshStage() {
            if (leadCaptured) {
                this.stage = Stage.COMPLETE;
                return;
            }
            if (!hasBudget()) {
                this.stage = Stage.COLLECT_BUDGET;
                return;
            }
            if (!hasPurpose()) {
                this.stage = Stage.COLLECT_PURPOSE;
                return;
            }
            if (!interestConfirmed) {
                this.stage = Stage.SUGGESTING;
                return;
            }
            if (!hasLeadName()) {
                this.stage = Stage.CAPTURE_NAME;
                return;
            }
            if (!hasLeadPhone()) {
                this.stage = Stage.CAPTURE_PHONE;
                return;
            }
            this.stage = Stage.COMPLETE;
        }

        AiAgentStateDTO toDto() {
            AiAgentStateDTO dto = new AiAgentStateDTO();
            dto.setBudgetMaxAed(budgetMaxAed);
            dto.setPurpose(purpose);
            dto.setPlan(plan);
            dto.setInterestConfirmed(interestConfirmed);
            dto.setLeadName(leadName);
            dto.setLeadPhone(leadPhone);
            dto.setLeadCaptured(leadCaptured);
            dto.setStage(stage != null ? stage.name() : Stage.COLLECT_BUDGET.name());
            dto.setMinArea(minArea);
            dto.setMaxArea(maxArea);
            dto.setCompletionYearFrom(completionYearFrom);
            dto.setCompletionYearTo(completionYearTo);
            dto.setCity(city);
            dto.setArea(area);
            return dto;
        }
    }

    private enum Stage {
        COLLECT_BUDGET,
        COLLECT_PURPOSE,
        COLLECT_PLAN,
        SUGGESTING,
        CAPTURE_NAME,
        CAPTURE_PHONE,
        COMPLETE,
    }
}
