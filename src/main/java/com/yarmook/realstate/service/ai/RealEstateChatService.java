package com.yarmook.realstate.service.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yarmook.realstate.config.ApplicationProperties;
import com.yarmook.realstate.domain.enumeration.ListingType;
import com.yarmook.realstate.service.ContactLeadService;
import com.yarmook.realstate.service.PropertyQueryService;
import com.yarmook.realstate.service.criteria.PropertyCriteria;
import com.yarmook.realstate.service.dto.AiAgentStateDTO;
import com.yarmook.realstate.service.dto.ChatRequestDTO;
import com.yarmook.realstate.service.dto.ChatRequestDTO.ConversationHistoryMessageDTO;
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
import java.util.LinkedHashSet;
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
        You are an expert UAE real estate advisor assistant. Your role is to act like a real real-estate agent and help clients find the right property in the UAE.

        Your conversation style:
        - Be warm, professional, and consultative
        - Ask smart qualifying questions to understand the client's real requirements before recommending options
        - Provide insights about the UAE property market when relevant
        - Be concise but informative
        - Sound like an experienced human agent, not a generic chatbot

        Qualification workflow (important):
        - Do NOT jump straight into recommendations if core requirements are missing
        - Ask ONE focused qualifying question at a time (maximum 2 questions in one message)
        - First collect the client's needs, then recommend matching properties from the provided data
        - If the user asks a broad question, answer briefly, then continue qualification
        - If the user asks for area information, area comparison, or general market suggestions, answer directly first (you may use provided property data/context), then ask a follow-up to personalize recommendations
        - If the user says "not sure" or asks "which one is better", give a short comparison and a practical recommendation based on what is already known, then ask ONE simpler follow-up question
        - When details are missing, explicitly ask for the next most important one

        Core information to gather before giving a shortlist (ask naturally):
        1. BUDGET: What is their approximate budget in AED? (e.g., 1M-3M AED)
        2. PURPOSE: Is this for investment, personal use, or rental income?
        3. LOCATION: Any preferred areas? (Dubai Marina, Downtown, Palm Jumeirah, etc.)
        4. TIMELINE: When do they need the property? (Ready now, off-plan 2025/2026?)
        5. TYPE: Apartment, villa, townhouse, or penthouse?

        When you have property data in the context:
        - Only recommend specific properties when the user has shared enough requirements
        - Highlight key selling points relevant to their stated needs
        - For investors: mention ROI potential, rental yields, capital appreciation areas
        - For end-users: mention lifestyle, amenities, family-friendliness
        - Always mention developer reputation when relevant
        - Explain why each recommendation matches the user's requirements

        Response format:
        - Keep responses under 200 words unless explaining properties
        - Use bullet points for property listings
        - Ask a follow-up question to keep the conversation going

        If the user seems ready to proceed, offer to connect them with an advisor.

        IMPORTANT: You MUST respond ONLY in valid JSON format with this structure:
        {
            "message": "Your conversational response here",
            "extractedInfo": {
                "budget": null or number in AED (only if clearly stated),
                "purpose": null or "INVESTMENT" or "END_USE" or "RENTAL",
                "location": null or "area name",
                "propertyType": null or "APARTMENT" or "VILLA" or "TOWNHOUSE" or "PENTHOUSE",
                "timeline": null or "READY" or year like 2025,
                "readyForAdvisor": false or true
            },
            "searchQuery": "semantic search query for properties based on qualified conversation" or null
        }
        """;

    private static final String ROUTING_PROMPT =
        """
        You are a routing classifier for a UAE real-estate assistant.
        Classify the user's latest message based on conversation state and recent history.

        Choose how the assistant should respond on this turn:
        - QUALIFY_FIRST: ask clarifying questions before giving a personalized recommendation
        - DIRECT_ANSWER: answer the user's question directly (area info, differences, general guidance), then optionally ask one follow-up
        - RAG_ANSWER: use internal property catalog / RAG data to answer or shortlist
        - WEB_SEARCH: live web search would improve the answer (market updates, new regulations, latest news, current community facts)
        - DECISION_SUPPORT: user is asking "which is better", "differences", "not sure", comparison, or wants help deciding with incomplete preferences

        Return ONLY valid JSON with this schema:
        {
          "mode": "QUALIFY_FIRST|DIRECT_ANSWER|RAG_ANSWER|WEB_SEARCH|DECISION_SUPPORT",
          "answerFirst": true|false,
          "useRag": true|false,
          "preferWebSearch": true|false,
          "showPropertyContext": true|false,
          "decisionSupport": true|false,
          "needsQualification": true|false,
          "reason": "short reason"
        }

        Rules:
        - If user asks about differences/comparison/best option or says they are not sure, prefer DECISION_SUPPORT.
        - If user asks for property availability, units under budget, projects, or a shortlist, prefer RAG_ANSWER.
        - If user asks area/community information or general advice, prefer DIRECT_ANSWER.
        - If current/up-to-date info is likely needed, prefer WEB_SEARCH.
        - If enough details are missing for personalized recommendations, set needsQualification=true.
        - Be conservative with WEB_SEARCH; use it only when freshness matters.
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
    private static final Pattern PROPERTY_TYPE_PATTERN = Pattern.compile("(?i)\\b(apartment|apts?|flat|villa|townhouse|penthouse)\\b");
    private static final Pattern MARINA_WORD_PATTERN = Pattern.compile("(?i)\\bmarina\\b");
    private static final Pattern FENCED_JSON_PATTERN = Pattern.compile("(?is)^```(?:json)?\\s*(.*?)\\s*```$");
    private static final int MAX_CONVERSATION_HISTORY_MESSAGES = 6;
    private static final int MAX_HISTORY_MESSAGE_CHARS = 400;
    private static final int MAX_WEB_RESULTS_IN_CONTEXT = 5;
    private static final int MAX_WEB_SOURCES_IN_REPLY = 4;
    private static final int MAX_WEB_QUERY_CHARS = 320;

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
    private final WebSearchClient webSearchClient;
    private final ObjectMapper objectMapper;

    public RealEstateChatService(
        ApplicationProperties applicationProperties,
        PropertyQueryService propertyQueryService,
        ContactLeadService contactLeadService,
        OpenAiClient openAiClient,
        QdrantClient qdrantClient,
        WebSearchClient webSearchClient,
        ObjectMapper objectMapper
    ) {
        this.applicationProperties = applicationProperties;
        this.propertyQueryService = propertyQueryService;
        this.contactLeadService = contactLeadService;
        this.openAiClient = openAiClient;
        this.qdrantClient = qdrantClient;
        this.webSearchClient = webSearchClient;
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
        List<ConversationTurn> conversationHistory = normalizeConversationHistory(request.getConversationHistory());

        try {
            ConversationStepResult result = processWithAI(state, userMessage, conversationHistory);
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
    private ConversationStepResult processWithAI(AgentState state, String userMessage, List<ConversationTurn> conversationHistory) {
        // Prime state with deterministic extraction so RAG and follow-up logic still work
        // even if the model returns malformed JSON or omits structured fields.
        extractInformationFromMessage(userMessage, state);

        TurnRoutingDecision routingDecision = classifyTurnRouting(userMessage, state, conversationHistory);
        List<WebSearchClient.WebSearchResult> webResults = routingDecision.shouldRunWebSearch()
            ? performLiveWebSearch(userMessage, state, conversationHistory, routingDecision)
            : Collections.emptyList();

        // Step 1: Build conversation context for ChatGPT
        String conversationContext = buildConversationContext(state, userMessage, conversationHistory, routingDecision);

        // Step 2: Perform semantic search using RAG if we have enough context,
        // or if the user is asking for area info / general suggestions.
        boolean readyForRecommendations = state.hasRecommendationRequirements();
        boolean exploratoryQuery = routingDecision.isExplorationOrInformation();
        boolean allowEarlySearch = readyForRecommendations || routingDecision.allowEarlySearch();
        List<PropertyContextDTO> ragResults = allowEarlySearch
            ? filterResultsByLocationPreference(performSemanticSearch(state, userMessage), state)
            : Collections.emptyList();

        // Step 3: Build property context for ChatGPT
        String propertyContext = buildPropertyContext(ragResults);
        String webSearchContext = buildWebSearchContext(webResults);

        // Step 4: Call ChatGPT for intelligent response
        String fullContext = conversationContext + "\n\n" + propertyContext + "\n\n" + webSearchContext;
        String aiResponse = callChatGPT(fullContext, userMessage);

        // Step 5: Parse AI response and extract structured data
        AiResponseData parsedResponse = parseAiResponse(aiResponse, state);

        // Step 6: Update state with extracted information
        updateStateFromAI(state, parsedResponse);

        readyForRecommendations = state.hasRecommendationRequirements();
        allowEarlySearch = readyForRecommendations || routingDecision.allowEarlySearch();

        // If AI extracted the missing requirements, run search now.
        if (allowEarlySearch && CollectionUtils.isEmpty(ragResults)) {
            ragResults = StringUtils.hasText(parsedResponse.searchQuery)
                ? performSemanticSearchWithQuery(parsedResponse.searchQuery)
                : performSemanticSearch(state, userMessage);
            ragResults = filterResultsByLocationPreference(ragResults, state);
        }

        // Step 7: If AI suggests more properties, search again with refined criteria
        if (allowEarlySearch && StringUtils.hasText(parsedResponse.searchQuery) && ragResults.size() < 3) {
            ragResults = mergePropertyContexts(ragResults, performSemanticSearchWithQuery(parsedResponse.searchQuery));
            ragResults = filterResultsByLocationPreference(ragResults, state);
        }

        // Step 8: Handle lead capture if user is ready
        if (parsedResponse.readyForAdvisor && state.isInterestConfirmed()) {
            return handleLeadCapture(state, ragResults, userMessage, parsedResponse.message);
        }

        // Step 9: Combine database results with RAG results for comprehensive coverage
        boolean exposePropertyContext = shouldExposePropertyContext(userMessage, state, routingDecision);
        List<PropertyContextDTO> combinedResults = exposePropertyContext && allowEarlySearch
            ? combineResults(ragResults, state)
            : Collections.emptyList();
        String reply = alignReplyWithConsultativeFlow(parsedResponse.message, state, userMessage, routingDecision);
        reply = appendWebSourcesToReply(reply, webResults, routingDecision);

        return ConversationStepResult.of(reply, combinedResults, state, false);
    }

    private String buildConversationContext(
        AgentState state,
        String userMessage,
        List<ConversationTurn> conversationHistory,
        TurnRoutingDecision routingDecision
    ) {
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
        if (StringUtils.hasText(state.getPlan())) {
            context.append("- Readiness Preference: ").append(state.getPlan()).append("\n");
        }
        if (StringUtils.hasText(state.getPropertyType())) {
            context.append("- Property Type: ").append(state.getPropertyType()).append("\n");
        }
        if (state.hasLeadName()) {
            context.append("- Client Name: ").append(state.getLeadName()).append("\n");
        }

        if (!CollectionUtils.isEmpty(conversationHistory)) {
            context.append("\nRecent conversation history (most recent last):\n");
            for (ConversationTurn turn : conversationHistory) {
                context
                    .append("- ")
                    .append("assistant".equals(turn.role()) ? "Assistant" : "User")
                    .append(": ")
                    .append(turn.content())
                    .append("\n");
            }
        }

        context.append("\nConversation stage: ").append(state.getStage().name());
        context.append("\nCore qualification complete: ").append(state.hasRecommendationRequirements() ? "YES" : "NO");
        context.append("\nRouter mode: ").append(routingDecision.mode());
        context.append("\nRouter answer first: ").append(routingDecision.answerFirst() ? "YES" : "NO");
        context.append("\nRouter use RAG: ").append(routingDecision.useRag() ? "YES" : "NO");
        context.append("\nRouter prefer web search: ").append(routingDecision.preferWebSearch() ? "YES" : "NO");
        if (routingDecision.preferWebSearch()) {
            context.append(
                "\nTooling note: Live web search is enabled when available; include source links in the final answer when web results are used."
            );
        }
        context.append("\nExploration/info query: ").append(routingDecision.isExplorationOrInformation() ? "YES" : "NO");
        if (!state.hasRecommendationRequirements()) {
            context.append("\nMissing requirements: ").append(describeMissingRequirements(state));
            if (routingDecision.isExplorationOrInformation()) {
                context.append(
                    "\nInstruction: Answer the user's area/info/suggestion question first, then ask one follow-up question to continue qualification."
                );
            } else {
                context.append("\nInstruction: Ask for the next missing requirement before recommending specific properties.");
            }
        }

        return context.toString();
    }

    private List<ConversationTurn> normalizeConversationHistory(List<ConversationHistoryMessageDTO> incomingHistory) {
        if (CollectionUtils.isEmpty(incomingHistory)) {
            return Collections.emptyList();
        }

        List<ConversationTurn> normalized = new ArrayList<>();
        for (ConversationHistoryMessageDTO message : incomingHistory) {
            if (message == null) {
                continue;
            }

            String role = normalizeConversationRole(message.getRole());
            String content = truncatePromptText(message.getContent(), MAX_HISTORY_MESSAGE_CHARS);
            if (role == null || !StringUtils.hasText(content)) {
                continue;
            }

            normalized.add(new ConversationTurn(role, content));
        }

        if (normalized.isEmpty()) {
            return Collections.emptyList();
        }

        int fromIndex = Math.max(0, normalized.size() - MAX_CONVERSATION_HISTORY_MESSAGES);
        return List.copyOf(normalized.subList(fromIndex, normalized.size()));
    }

    private String normalizeConversationRole(String role) {
        if (!StringUtils.hasText(role)) {
            return null;
        }
        String normalized = role.trim().toLowerCase(Locale.ENGLISH);
        return switch (normalized) {
            case "user", "assistant" -> normalized;
            default -> null;
        };
    }

    private String truncatePromptText(String text, int maxLength) {
        if (!StringUtils.hasText(text)) {
            return null;
        }
        String normalized = text.trim().replaceAll("\\s+", " ");
        if (normalized.length() <= maxLength) {
            return normalized;
        }
        return normalized.substring(0, Math.max(0, maxLength - 3)).trim() + "...";
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
            // Try to parse as JSON (including common fenced-markdown responses)
            JsonNode root = objectMapper.readTree(extractJsonCandidate(aiResponse));

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
                if (info.has("timeline") && !info.get("timeline").isNull()) {
                    data.timeline = info.get("timeline").asText();
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
            String normalizedLocation = normalizeAreaAlias(aiData.location, null);
            String lower = normalizedLocation.toLowerCase(Locale.ENGLISH);
            if (KNOWN_CITIES.contains(lower)) {
                state.setCity(capitalize(normalizedLocation));
            } else {
                state.setArea(capitalize(normalizedLocation));
            }
        }
        if (StringUtils.hasText(aiData.propertyType)) {
            state.setPropertyType(normalizePropertyType(aiData.propertyType));
        }
        applyTimelinePreference(state, aiData.timeline);
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
        if (StringUtils.hasText(state.getPlan())) {
            if ("READY".equalsIgnoreCase(state.getPlan())) {
                query.append("ready to move in ready property ");
            } else if ("OFF_PLAN".equalsIgnoreCase(state.getPlan())) {
                query.append("off plan new launch payment plan ");
            }
        }
        if (StringUtils.hasText(state.getPropertyType())) {
            query.append(state.getPropertyType().toLowerCase(Locale.ENGLISH)).append(" ");
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
        List<PropertyContextDTO> combined = new ArrayList<>(filterResultsByLocationPreference(ragResults, state));

        // If RAG didn't find enough, supplement with database query
        if (combined.size() < 5) {
            List<PropertyContextDTO> dbResults = filterResultsByLocationPreference(findMatchingProperties(state), state);
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

    private List<PropertyContextDTO> filterResultsByLocationPreference(List<PropertyContextDTO> results, AgentState state) {
        if (CollectionUtils.isEmpty(results) || state == null || !state.hasLocationRequirement()) {
            return CollectionUtils.isEmpty(results) ? Collections.emptyList() : results;
        }

        List<PropertyContextDTO> filtered = results.stream().filter(dto -> matchesStateLocationPreference(dto, state)).toList();
        return filtered.isEmpty() ? results : filtered;
    }

    private boolean matchesStateLocationPreference(PropertyContextDTO dto, AgentState state) {
        if (dto == null || state == null) {
            return false;
        }

        String dtoArea = normalizeLocationToken(dto.getArea());
        String dtoCity = normalizeLocationToken(dto.getCity());
        String combined = (dtoArea + " " + dtoCity).trim();

        if (StringUtils.hasText(state.getArea())) {
            String stateArea = normalizeLocationToken(normalizeAreaAlias(state.getArea(), state.getCity()));
            if (!StringUtils.hasText(stateArea)) {
                return true;
            }

            if (dtoArea.contains(stateArea) || combined.contains(stateArea)) {
                return true;
            }

            return allLocationTokensPresent(stateArea, combined);
        }

        if (StringUtils.hasText(state.getCity())) {
            String stateCity = normalizeLocationToken(state.getCity());
            if (!StringUtils.hasText(stateCity)) {
                return true;
            }
            return dtoCity.contains(stateCity) || combined.contains(stateCity) || allLocationTokensPresent(stateCity, combined);
        }

        return true;
    }

    private boolean allLocationTokensPresent(String expected, String actual) {
        if (!StringUtils.hasText(expected) || !StringUtils.hasText(actual)) {
            return false;
        }
        List<String> tokens = Arrays.stream(expected.split("\\s+"))
            .map(String::trim)
            .filter(StringUtils::hasText)
            .filter(token -> token.length() > 1)
            .toList();
        if (tokens.isEmpty()) {
            return false;
        }
        for (String token : tokens) {
            if (!actual.contains(token)) {
                return false;
            }
        }
        return true;
    }

    private List<PropertyContextDTO> mergePropertyContexts(List<PropertyContextDTO> primary, List<PropertyContextDTO> secondary) {
        List<PropertyContextDTO> merged = new ArrayList<>();
        if (!CollectionUtils.isEmpty(primary)) {
            merged.addAll(primary);
        }
        if (!CollectionUtils.isEmpty(secondary)) {
            for (PropertyContextDTO candidate : secondary) {
                boolean alreadyExists = merged.stream().anyMatch(existing -> sameProperty(existing, candidate));
                if (!alreadyExists) {
                    merged.add(candidate);
                }
            }
        }
        return merged.stream().limit(8).toList();
    }

    private boolean sameProperty(PropertyContextDTO left, PropertyContextDTO right) {
        if (left == null || right == null) {
            return false;
        }
        if (left.getExtId() != null && right.getExtId() != null) {
            return Objects.equals(left.getExtId(), right.getExtId());
        }
        if (left.getPropertyId() != null && right.getPropertyId() != null) {
            return Objects.equals(left.getPropertyId(), right.getPropertyId());
        }
        return (
            StringUtils.hasText(left.getName()) && StringUtils.hasText(right.getName()) && left.getName().equalsIgnoreCase(right.getName())
        );
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

        TurnRoutingDecision routingDecision = classifyTurnRouting(userMessage, state, Collections.emptyList());

        // Find matching properties
        boolean readyForRecommendations = state.hasRecommendationRequirements();
        boolean exploratoryQuery = routingDecision.isExplorationOrInformation();
        List<PropertyContextDTO> contextEntries = readyForRecommendations || exploratoryQuery
            ? filterResultsByLocationPreference(findMatchingProperties(state), state)
            : Collections.emptyList();

        // Build response based on state
        String reply = buildRuleBasedResponse(state, contextEntries, userMessage, routingDecision);

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

        // Extract readiness and property type
        extractTimelinePreference(userMessage, state);
        String propertyType = extractPropertyType(userMessage);
        if (StringUtils.hasText(propertyType)) {
            state.setPropertyType(propertyType);
        }
    }

    private String buildRuleBasedResponse(
        AgentState state,
        List<PropertyContextDTO> properties,
        String userMessage,
        TurnRoutingDecision routingDecision
    ) {
        StringBuilder response = new StringBuilder();
        boolean readyForRecommendations = state.hasRecommendationRequirements();
        boolean exploratoryQuery = routingDecision.isExplorationOrInformation();

        // If we found properties, show them
        if ((readyForRecommendations || exploratoryQuery) && !CollectionUtils.isEmpty(properties)) {
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

        // Ask qualifying questions based on what's missing before recommending from the catalog.
        if (!readyForRecommendations) {
            if (response.length() > 0) {
                response.append("\n");
            }
            response.append(alignReplyWithConsultativeFlow(null, state, userMessage, routingDecision));
        } else if (!state.isInterestConfirmed()) {
            response.append("Would you like me to connect you with one of our property advisors for more details?");
        }

        return response.toString();
    }

    private List<WebSearchClient.WebSearchResult> performLiveWebSearch(
        String userMessage,
        AgentState state,
        List<ConversationTurn> conversationHistory,
        TurnRoutingDecision routingDecision
    ) {
        if (webSearchClient == null || !webSearchClient.isEnabled() || !routingDecision.shouldRunWebSearch()) {
            return Collections.emptyList();
        }

        String webQuery = buildLiveWebSearchQuery(userMessage, state, conversationHistory, routingDecision);
        if (!StringUtils.hasText(webQuery)) {
            return Collections.emptyList();
        }

        List<WebSearchClient.WebSearchResult> results = webSearchClient.search(webQuery, MAX_WEB_RESULTS_IN_CONTEXT);
        if (CollectionUtils.isEmpty(results) && !Objects.equals(webQuery, userMessage)) {
            // Fallback to the raw user query if the contextual query was too specific.
            results = webSearchClient.search(truncateForSearchQuery(userMessage), MAX_WEB_RESULTS_IN_CONTEXT);
        }
        return CollectionUtils.isEmpty(results) ? Collections.emptyList() : results;
    }

    private String buildLiveWebSearchQuery(
        String userMessage,
        AgentState state,
        List<ConversationTurn> conversationHistory,
        TurnRoutingDecision routingDecision
    ) {
        if (!StringUtils.hasText(userMessage)) {
            return null;
        }

        StringBuilder query = new StringBuilder(userMessage.trim());

        if (state != null) {
            if (state.hasLocationRequirement()) {
                query.append(" ");
                if (StringUtils.hasText(state.getArea())) {
                    query.append(state.getArea()).append(" ");
                }
                if (StringUtils.hasText(state.getCity())) {
                    query.append(state.getCity()).append(" ");
                }
            }
            if (state.hasPurpose()) {
                query.append(" uae real estate ").append(humanizePurpose(state.getPurpose()));
            }
        }

        if (routingDecision != null && routingDecision.decisionSupport()) {
            query.append(" compare communities investment dubai uae");
        } else if (routingDecision != null && routingDecision.preferWebSearch()) {
            query.append(" UAE real estate latest");
        }

        if (isContextDependentFollowUp(userMessage)) {
            String historyContext = buildHistoryContextForSearch(conversationHistory);
            if (StringUtils.hasText(historyContext)) {
                query.append(" ").append(historyContext);
            }
        }

        return truncateForSearchQuery(query.toString());
    }

    private boolean isContextDependentFollowUp(String userMessage) {
        if (!StringUtils.hasText(userMessage)) {
            return false;
        }
        String normalized = userMessage.trim().toLowerCase(Locale.ENGLISH);
        if (normalized.length() > 40) {
            return false;
        }
        return containsAny(
            normalized,
            "what are differences",
            "what are the differences",
            "what are diffrences",
            "difference",
            "differences",
            "compare",
            "which one",
            "better",
            "not sure",
            "which is best"
        );
    }

    private String buildHistoryContextForSearch(List<ConversationTurn> conversationHistory) {
        if (CollectionUtils.isEmpty(conversationHistory)) {
            return null;
        }

        List<ConversationTurn> recentTurns = conversationHistory.subList(
            Math.max(0, conversationHistory.size() - 3),
            conversationHistory.size()
        );
        List<String> fragments = new ArrayList<>();

        for (ConversationTurn turn : recentTurns) {
            if (!"user".equals(turn.role()) && !"assistant".equals(turn.role())) {
                continue;
            }
            String content = truncatePromptText(turn.content(), 140);
            if (StringUtils.hasText(content)) {
                fragments.add(content);
            }
        }

        if (fragments.isEmpty()) {
            return null;
        }

        String combined = String.join(" ", fragments);
        return truncateForSearchQuery(combined);
    }

    private String truncateForSearchQuery(String query) {
        if (!StringUtils.hasText(query)) {
            return null;
        }
        String normalized = query.trim().replaceAll("\\s+", " ");
        if (normalized.length() <= MAX_WEB_QUERY_CHARS) {
            return normalized;
        }
        return normalized.substring(0, MAX_WEB_QUERY_CHARS).trim();
    }

    private String buildWebSearchContext(List<WebSearchClient.WebSearchResult> webResults) {
        if (CollectionUtils.isEmpty(webResults)) {
            return "Live web search results: none used for this turn.";
        }

        StringBuilder context = new StringBuilder("Live web search results (up-to-date external sources):\n");
        int limit = Math.min(MAX_WEB_RESULTS_IN_CONTEXT, webResults.size());
        for (int i = 0; i < limit; i++) {
            WebSearchClient.WebSearchResult result = webResults.get(i);
            context.append(i + 1).append(". ");
            context.append(StringUtils.hasText(result.title()) ? result.title() : "Source");
            if (StringUtils.hasText(result.url())) {
                context.append("\n   URL: ").append(result.url());
            }
            if (StringUtils.hasText(result.snippet())) {
                context.append("\n   Snippet: ").append(result.snippet());
            }
            context.append("\n");
        }
        context.append("Use these results for current facts and mention source links in the answer when relying on web info.");
        return context.toString();
    }

    private String appendWebSourcesToReply(
        String reply,
        List<WebSearchClient.WebSearchResult> webResults,
        TurnRoutingDecision routingDecision
    ) {
        if (
            !StringUtils.hasText(reply) ||
            CollectionUtils.isEmpty(webResults) ||
            routingDecision == null ||
            !routingDecision.preferWebSearch()
        ) {
            return reply;
        }

        String lower = reply.toLowerCase(Locale.ENGLISH);
        if (lower.contains("sources:") || lower.contains("source:")) {
            return reply;
        }

        LinkedHashSet<String> seenUrls = new LinkedHashSet<>();
        StringBuilder builder = new StringBuilder(reply.trim());
        builder.append("\n\nSources:\n");

        int added = 0;
        for (WebSearchClient.WebSearchResult result : webResults) {
            if (result == null || !StringUtils.hasText(result.url()) || !seenUrls.add(result.url())) {
                continue;
            }
            String label = StringUtils.hasText(result.title())
                ? result.title()
                : Objects.requireNonNullElse(result.displayUrl(), result.url());
            builder.append("- ").append(label).append(": ").append(result.url()).append("\n");
            added++;
            if (added >= MAX_WEB_SOURCES_IN_REPLY) {
                break;
            }
        }

        return added > 0 ? builder.toString().trim() : reply;
    }

    private TurnRoutingDecision classifyTurnRouting(String userMessage, AgentState state, List<ConversationTurn> conversationHistory) {
        if (!StringUtils.hasText(userMessage)) {
            return heuristicRoutingDecision(userMessage, state);
        }

        try {
            String routingContext = buildRoutingClassifierContext(state, conversationHistory);
            String routingResponse = openAiClient.chat(ROUTING_PROMPT, routingContext, userMessage);
            TurnRoutingDecision decision = parseTurnRoutingDecision(routingResponse, state);
            return decision;
        } catch (Exception ex) {
            LOG.warn("LLM routing classifier failed, using heuristic fallback: {}", ex.getMessage());
            return heuristicRoutingDecision(userMessage, state);
        }
    }

    private String buildRoutingClassifierContext(AgentState state, List<ConversationTurn> conversationHistory) {
        StringBuilder context = new StringBuilder();
        context.append("Current qualification state:\n");
        if (state != null) {
            context
                .append("- Qualified for personalized shortlist: ")
                .append(state.hasRecommendationRequirements() ? "YES" : "NO")
                .append("\n");
            if (state.hasBudget()) {
                context.append("- Budget: ").append(formatAed(state.getBudgetMaxAed())).append("\n");
            }
            if (state.hasPurpose()) {
                context.append("- Purpose: ").append(state.getPurpose()).append("\n");
            }
            if (state.hasLocationRequirement()) {
                context
                    .append("- Location: ")
                    .append(
                        String.join(", ", Arrays.asList(state.getArea(), state.getCity()).stream().filter(StringUtils::hasText).toList())
                    )
                    .append("\n");
            }
            if (state.hasPropertyTypePreference()) {
                context.append("- Property type: ").append(state.getPropertyType()).append("\n");
            }
            if (state.hasTimelineRequirement()) {
                context.append("- Timeline: ");
                if (StringUtils.hasText(state.getPlan())) {
                    context.append(state.getPlan());
                } else {
                    context.append(
                        String.join(
                            " to ",
                            Arrays.asList(state.getCompletionYearFrom(), state.getCompletionYearTo())
                                .stream()
                                .filter(Objects::nonNull)
                                .map(String::valueOf)
                                .toList()
                        )
                    );
                }
                context.append("\n");
            }
        }

        if (!CollectionUtils.isEmpty(conversationHistory)) {
            context.append("\nRecent messages:\n");
            int fromIndex = Math.max(0, conversationHistory.size() - 4);
            for (ConversationTurn turn : conversationHistory.subList(fromIndex, conversationHistory.size())) {
                context.append("- ").append(turn.role()).append(": ").append(turn.content()).append("\n");
            }
        }

        return context.toString();
    }

    private TurnRoutingDecision parseTurnRoutingDecision(String routingResponse, AgentState state) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(extractJsonCandidate(routingResponse));

        String mode = Optional.ofNullable(root.get("mode")).map(JsonNode::asText).orElse("QUALIFY_FIRST");
        mode = mode.trim().toUpperCase(Locale.ENGLISH);

        boolean answerFirst = readBooleanOrDefault(root, "answerFirst", false);
        boolean useRag = readBooleanOrDefault(root, "useRag", false);
        boolean preferWebSearch = readBooleanOrDefault(root, "preferWebSearch", false);
        boolean showPropertyContext = readBooleanOrDefault(root, "showPropertyContext", false);
        boolean decisionSupport = readBooleanOrDefault(root, "decisionSupport", false);
        boolean needsQualification = readBooleanOrDefault(
            root,
            "needsQualification",
            state == null || !state.hasRecommendationRequirements()
        );
        String reason = Optional.ofNullable(root.get("reason")).map(JsonNode::asText).orElse(null);

        if ("DECISION_SUPPORT".equals(mode)) {
            decisionSupport = true;
            answerFirst = true;
        } else if ("DIRECT_ANSWER".equals(mode)) {
            answerFirst = true;
        } else if ("RAG_ANSWER".equals(mode)) {
            useRag = true;
            showPropertyContext = true;
        } else if ("WEB_SEARCH".equals(mode)) {
            preferWebSearch = true;
            answerFirst = true;
        } else if (!"QUALIFY_FIRST".equals(mode)) {
            mode = "QUALIFY_FIRST";
        }

        if (state != null && state.hasRecommendationRequirements()) {
            // Once fully qualified, prefer using RAG/catalog for personalization unless the router explicitly avoids it.
            useRag = useRag || "QUALIFY_FIRST".equals(mode);
            showPropertyContext = showPropertyContext || useRag;
            needsQualification = false;
        }

        return new TurnRoutingDecision(
            mode,
            answerFirst,
            useRag,
            preferWebSearch,
            showPropertyContext,
            decisionSupport,
            needsQualification,
            reason
        );
    }

    private boolean readBooleanOrDefault(JsonNode root, String fieldName, boolean defaultValue) {
        return root.has(fieldName) && !root.get(fieldName).isNull() ? root.get(fieldName).asBoolean(defaultValue) : defaultValue;
    }

    private TurnRoutingDecision heuristicRoutingDecision(String userMessage, AgentState state) {
        boolean decisionSupport = isDecisionSupportTurnHeuristic(userMessage, state);
        boolean recommendation = isRecommendationOrPropertyQueryHeuristic(userMessage, state);
        boolean exploration = isExplorationOrInformationQueryHeuristic(userMessage, state) || decisionSupport;
        boolean qualified = state != null && state.hasRecommendationRequirements();

        String mode;
        if (decisionSupport) {
            mode = "DECISION_SUPPORT";
        } else if (recommendation) {
            mode = "RAG_ANSWER";
        } else if (exploration) {
            mode = "DIRECT_ANSWER";
        } else {
            mode = "QUALIFY_FIRST";
        }

        boolean useRag = recommendation || (qualified && exploration);
        boolean showPropertyContext = recommendation || qualified;
        boolean answerFirst = exploration || decisionSupport;
        boolean needsQualification = !qualified;

        return new TurnRoutingDecision(
            mode,
            answerFirst,
            useRag,
            false,
            showPropertyContext,
            decisionSupport,
            needsQualification,
            "heuristic_fallback"
        );
    }

    private boolean shouldExposePropertyContext(String userMessage, AgentState state, TurnRoutingDecision routingDecision) {
        if (state == null) {
            return false;
        }
        return state.hasRecommendationRequirements() || routingDecision.isRecommendationOrProperty();
    }

    // Heuristic fallback only (used when LLM routing classification fails).
    private boolean isExplorationOrInformationQueryHeuristic(String userMessage, AgentState state) {
        if (!StringUtils.hasText(userMessage)) {
            return false;
        }
        String lower = userMessage.toLowerCase(Locale.ENGLISH);

        if (containsAny(lower, "area info", "area information", "neighborhood", "community", "location info", "tell me about", "compare")) {
            return true;
        }

        if (
            containsAny(
                lower,
                "suggest",
                "recommend",
                "recommendation",
                "best area",
                "which area",
                "where should",
                "good area",
                "good place",
                "options",
                "ideas"
            )
        ) {
            return true;
        }

        if (containsAny(lower, "roi", "rental yield", "capital appreciation", "family-friendly", "family friendly", "schools")) {
            return true;
        }

        if (
            (containsAny(lower, "about", "is ") && hasLocationMention(userMessage, state)) ||
            containsAny(lower, "pros and cons", "vs ", " versus ")
        ) {
            return true;
        }

        return isRecommendationOrPropertyQueryHeuristic(userMessage, state);
    }

    // Heuristic fallback only (used when LLM routing classification fails).
    private boolean isRecommendationOrPropertyQueryHeuristic(String userMessage, AgentState state) {
        if (!StringUtils.hasText(userMessage)) {
            return false;
        }
        String lower = userMessage.toLowerCase(Locale.ENGLISH);

        if (
            containsAny(
                lower,
                "property",
                "properties",
                "project",
                "projects",
                "listing",
                "listings",
                "launch",
                "launching",
                "off-plan",
                "off plan",
                "show me",
                "find me"
            )
        ) {
            return true;
        }

        if (containsAny(lower, "recommend", "recommendation", "suggest", "suggestion")) {
            return true;
        }

        return (
            hasLocationMention(userMessage, state) && containsAny(lower, "what's available", "what is available", "options", "available")
        );
    }

    private boolean hasLocationMention(String userMessage, AgentState state) {
        if ((state != null && state.hasLocationRequirement()) || !StringUtils.hasText(userMessage)) {
            return state != null && state.hasLocationRequirement();
        }

        String lower = userMessage.toLowerCase(Locale.ENGLISH);
        for (String area : KNOWN_AREAS) {
            if (lower.contains(area)) {
                return true;
            }
        }
        for (String city : KNOWN_CITIES) {
            if (lower.contains(city)) {
                return true;
            }
        }
        return false;
    }

    private String buildDecisionSupportReply(String aiMessage, AgentState state, String userMessage, TurnRoutingDecision routingDecision) {
        if (!isDecisionSupportTurn(userMessage, state, routingDecision)) {
            return null;
        }

        // Let the AI response pass through if it already provides guidance and is not just repeating qualification questions.
        if (StringUtils.hasText(aiMessage) && !isLikelyQualificationLoop(aiMessage, state)) {
            return null;
        }

        String summary = buildQualificationSummary(state);
        String guidance = buildDecisionSupportGuidance(state);
        String followUp = buildDecisionSupportFollowUpQuestion(state);

        StringBuilder reply = new StringBuilder();
        if (StringUtils.hasText(summary)) {
            reply.append(summary).append(" ");
        }
        reply.append(guidance);
        if (StringUtils.hasText(followUp)) {
            reply.append(" ").append(followUp);
        }
        return reply.toString().trim();
    }

    private boolean isDecisionSupportTurn(String userMessage, AgentState state, TurnRoutingDecision routingDecision) {
        if (routingDecision != null) {
            return routingDecision.decisionSupport();
        }
        return isDecisionSupportTurnHeuristic(userMessage, state);
    }

    // Heuristic fallback only (used when LLM routing classification fails).
    private boolean isDecisionSupportTurnHeuristic(String userMessage, AgentState state) {
        if (!StringUtils.hasText(userMessage) || state == null || state.hasRecommendationRequirements()) {
            return false;
        }
        String lower = userMessage.toLowerCase(Locale.ENGLISH);

        if (containsAny(lower, "not sure", "unsure", "don't know", "dont know", "you decide", "anything is fine", "no preference")) {
            return true;
        }

        return containsAny(
            lower,
            "which one better",
            "which one is better",
            "whitch one better",
            "what is better",
            "what are differences",
            "what are the differences",
            "what are diffrences",
            "what are the diffrences",
            "difference",
            "differences",
            "diffrence",
            "diffrences",
            "how are they different",
            "how do they differ",
            "compare them",
            "comparison",
            "better option",
            "best option",
            "which is best"
        );
    }

    private boolean isLikelyQualificationLoop(String aiMessage, AgentState state) {
        if (!StringUtils.hasText(aiMessage)) {
            return true;
        }
        String lower = aiMessage.toLowerCase(Locale.ENGLISH);
        if (!aiMessageAlreadyAsksCurrentQualification(aiMessage, state)) {
            return false;
        }
        return !containsAny(lower, "depends", "typically", "generally", "for investment", "for end-use", "because", "better");
    }

    private String buildDecisionSupportGuidance(AgentState state) {
        if (state == null) {
            return "I can help narrow it down quickly by comparing options based on budget, goal, and timing.";
        }

        String purpose = Optional.ofNullable(state.getPurpose()).orElse("").toUpperCase(Locale.ENGLISH);
        boolean hasBudget = state.hasBudget();
        boolean hasLocation = state.hasLocationRequirement();
        boolean hasType = state.hasPropertyTypePreference();

        if ("INVESTMENT".equals(purpose) || "INVEST".equals(purpose)) {
            if (!hasLocation) {
                String budgetText = hasBudget ? " at " + formatAed(state.getBudgetMaxAed()) + " budget" : "";
                if (!hasType) {
                    return (
                        "For investment" +
                        budgetText +
                        ", apartments are usually the safer starting point because they have better liquidity and broader rental demand. " +
                        "A practical comparison to start with is: JVC (strong tenant demand and many options), Town Square (good value and family tenant appeal), and Dubai Silicon Oasis (value-focused entry and steady rental demand). " +
                        "If you want one default starting point, begin with JVC for balance of demand and resale liquidity."
                    );
                }
                return (
                    "For investment" +
                    budgetText +
                    ", a good way to decide is by strategy: JVC for balanced rental demand and liquidity, Town Square for value and family tenants, and Dubai Silicon Oasis for budget-conscious yield-focused buyers. " +
                    "If you are undecided, JVC is usually the safest first area to evaluate."
                );
            }

            if (!hasType) {
                return (
                    "For investment in " +
                    String.join(", ", Arrays.asList(state.getArea(), state.getCity()).stream().filter(StringUtils::hasText).toList()) +
                    ", apartments are usually better than villas/townhouses at this stage because entry price is lower, turnover is faster, and rental demand is broader."
                );
            }

            if (!state.hasTimelineRequirement()) {
                return "For investment, the main decision now is yield-now vs appreciation-later: ready units suit immediate rental income, while off-plan may offer stronger upside and payment plan flexibility.";
            }

            return "For investment, the better option depends on whether you prioritize immediate rental yield, capital appreciation potential, or resale liquidity. I can narrow it down once we pick the main priority.";
        }

        if ("END_USE".equals(purpose)) {
            return (
                "If you're not sure yet, the best option depends on lifestyle priorities: commute access, family amenities/schools, unit size, and whether you want a ready home or off-plan value. " +
                "I can guide you step by step and suggest matching communities."
            );
        }

        if ("RENTAL".equals(purpose)) {
            return "For rental-income focused buying, the better option is usually the one with stronger tenant demand, better net yield, and easier re-letting rather than just the lowest price.";
        }

        return "I can compare options for you. The best choice usually depends on your goal (investment vs living), budget flexibility, and whether you want ready or off-plan.";
    }

    private String buildDecisionSupportFollowUpQuestion(AgentState state) {
        if (state == null) {
            return "What should we optimize first: investment return, lifestyle, or lowest entry price?";
        }

        String purpose = Optional.ofNullable(state.getPurpose()).orElse("").toUpperCase(Locale.ENGLISH);
        if (("INVESTMENT".equals(purpose) || "INVEST".equals(purpose)) && !state.hasLocationRequirement()) {
            return "To narrow this down, should I prioritize higher rental yield, stronger capital appreciation, or easier resale/liquidity?";
        }

        if (!state.hasTimelineRequirement()) {
            return "Would you prefer a ready unit for immediate rental income, or off-plan for potential appreciation and payment plans?";
        }

        if (!state.hasPropertyTypePreference()) {
            return "To narrow this down, would you like me to focus on apartments first (usually better for investment liquidity) or include townhouses/villas as well?";
        }

        if (!state.hasLocationRequirement()) {
            return "To personalize the shortlist, should I prioritize higher yield, stronger capital appreciation areas, or family-tenant demand?";
        }

        return "What matters most for your decision: rental yield, appreciation potential, or ease of resale?";
    }

    private String alignReplyWithConsultativeFlow(
        String aiMessage,
        AgentState state,
        String userMessage,
        TurnRoutingDecision routingDecision
    ) {
        if (state == null || state.hasRecommendationRequirements()) {
            return StringUtils.hasText(aiMessage) ? aiMessage : "";
        }

        String decisionSupportReply = buildDecisionSupportReply(aiMessage, state, userMessage, routingDecision);
        if (StringUtils.hasText(decisionSupportReply)) {
            return decisionSupportReply;
        }

        String nextQuestion = buildNextQualificationQuestion(state);
        if (!StringUtils.hasText(nextQuestion)) {
            return StringUtils.hasText(aiMessage) ? aiMessage : "";
        }

        if (aiMessageAlreadyAsksCurrentQualification(aiMessage, state)) {
            return aiMessage;
        }

        String summary = buildQualificationSummary(state);
        if (!StringUtils.hasText(aiMessage)) {
            if (StringUtils.hasText(summary)) {
                return "I can help shortlist properties from our listings. " + summary + " " + nextQuestion;
            }
            return "I can help shortlist properties from our listings. " + nextQuestion;
        }

        StringBuilder reply = new StringBuilder(aiMessage.trim());
        reply.append("\n\n");
        if (StringUtils.hasText(summary)) {
            reply.append(summary).append(" ");
        }
        reply.append(nextQuestion);
        return reply.toString();
    }

    private String buildQualificationSummary(AgentState state) {
        List<String> items = new ArrayList<>();
        if (state.hasBudget()) {
            items.add("Budget noted (" + formatAed(state.getBudgetMaxAed()) + ").");
        }
        if (state.hasPurpose()) {
            items.add("Purpose noted (" + humanizePurpose(state.getPurpose()) + ").");
        }
        if (state.hasLocationRequirement()) {
            items.add(
                "Location noted (" +
                String.join(", ", Arrays.asList(state.getArea(), state.getCity()).stream().filter(StringUtils::hasText).toList()) +
                ")."
            );
        }
        if (state.hasPropertyTypePreference()) {
            items.add("Type noted (" + humanizePropertyType(state.getPropertyType()) + ").");
        }
        if (state.hasTimelineRequirement()) {
            items.add("Timeline preference noted.");
        }
        if (items.isEmpty()) {
            return null;
        }
        return "So far: " + String.join(" ", items);
    }

    private String buildNextQualificationQuestion(AgentState state) {
        if (!state.hasBudget()) {
            return "What budget range are you targeting in AED (for example, AED 1.5M to AED 2M)?";
        }
        if (!state.hasPurpose()) {
            return "Is this purchase mainly for investment, rental income, or personal/family use?";
        }
        if (!state.hasLocationRequirement()) {
            return "Which area or city do you prefer (for example Dubai Marina, Downtown Dubai, Dubai Hills, or Abu Dhabi)?";
        }
        if (!state.hasPropertyTypePreference()) {
            return "What property type are you looking for: apartment, villa, townhouse, or penthouse?";
        }
        if (!state.hasTimelineRequirement()) {
            return "Do you need a ready property now, or are you open to off-plan with a target handover year?";
        }
        return null;
    }

    private String describeMissingRequirements(AgentState state) {
        List<String> missing = new ArrayList<>();
        if (!state.hasBudget()) {
            missing.add("budget");
        }
        if (!state.hasPurpose()) {
            missing.add("purpose");
        }
        if (!state.hasLocationRequirement()) {
            missing.add("location");
        }
        if (!state.hasPropertyTypePreference()) {
            missing.add("property type");
        }
        if (!state.hasTimelineRequirement()) {
            missing.add("timeline");
        }
        return missing.isEmpty() ? "none" : String.join(", ", missing);
    }

    private boolean aiMessageAlreadyAsksCurrentQualification(String aiMessage, AgentState state) {
        if (!StringUtils.hasText(aiMessage) || !aiMessage.contains("?")) {
            return false;
        }
        String lower = aiMessage.toLowerCase(Locale.ENGLISH);

        if (!state.hasBudget()) {
            return containsAny(lower, "budget", "aed", "price range");
        }
        if (!state.hasPurpose()) {
            return containsAny(lower, "investment", "rental", "personal use", "family use", "purpose");
        }
        if (!state.hasLocationRequirement()) {
            return containsAny(lower, "location", "area", "community", "city", "where");
        }
        if (!state.hasPropertyTypePreference()) {
            return containsAny(lower, "property type", "apartment", "villa", "townhouse", "penthouse");
        }
        if (!state.hasTimelineRequirement()) {
            return containsAny(lower, "timeline", "ready", "off-plan", "handover", "when");
        }
        return false;
    }

    private boolean containsAny(String text, String... tokens) {
        if (!StringUtils.hasText(text) || tokens == null) {
            return false;
        }
        for (String token : tokens) {
            if (StringUtils.hasText(token) && text.contains(token)) {
                return true;
            }
        }
        return false;
    }

    private String humanizePurpose(String purpose) {
        if (!StringUtils.hasText(purpose)) {
            return "not specified";
        }
        return switch (purpose.trim().toUpperCase(Locale.ENGLISH)) {
            case "INVESTMENT", "INVEST" -> "investment";
            case "RENTAL" -> "rental income";
            case "END_USE" -> "end use";
            default -> purpose.toLowerCase(Locale.ENGLISH);
        };
    }

    private String humanizePropertyType(String propertyType) {
        if (!StringUtils.hasText(propertyType)) {
            return "not specified";
        }
        return propertyType.trim().toLowerCase(Locale.ENGLISH).replace('_', ' ');
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
        contexts = filterResultsByLocationPreference(contexts, state);

        Double budgetMax = state.getBudgetMaxAed();
        if (budgetMax == null) {
            return contexts.stream().limit(5).toList();
        }

        List<PropertyContextDTO> filtered = contexts.stream().filter(dto -> priceWithinBudget(dto, budgetMax)).limit(5).toList();

        if (!filtered.isEmpty()) {
            return filtered;
        }

        return filterResultsByLocationPreference(fallbackByMinPrice(state, budgetMax), state);
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

        String aliasedArea = normalizeAreaAlias(lower, null);
        if (StringUtils.hasText(aliasedArea) && !aliasedArea.equals(lower)) {
            state.setArea(capitalize(aliasedArea));
            return;
        }

        for (String city : KNOWN_CITIES) {
            if (lower.contains(city)) {
                state.setCity(capitalize(city));
                return;
            }
        }
    }

    private String normalizeAreaAlias(String location, String cityHint) {
        if (!StringUtils.hasText(location)) {
            return location;
        }
        String lower = location.trim().toLowerCase(Locale.ENGLISH);
        String normalizedCityHint = cityHint != null ? cityHint.trim().toLowerCase(Locale.ENGLISH) : "";

        // Common shorthand used by users in UAE real estate chats.
        if (MARINA_WORD_PATTERN.matcher(lower).find()) {
            boolean explicitAbuDhabi =
                lower.contains("abu dhabi") || normalizedCityHint.contains("abu dhabi") || lower.contains("al marina");
            if (!explicitAbuDhabi) {
                return "dubai marina";
            }
        }

        if ("downtown".equals(lower) || lower.contains(" downtown ")) {
            return "downtown dubai";
        }

        return lower;
    }

    private String normalizeLocationToken(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        return value.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9\\s]", " ").replaceAll("\\s+", " ").trim();
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
        if (StringUtils.hasText(state.getPlan())) {
            builder.append("Timeline: ").append(state.getPlan()).append(" | ");
        }
        if (StringUtils.hasText(state.getPropertyType())) {
            builder.append("Type: ").append(state.getPropertyType()).append(" | ");
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
        if (StringUtils.hasText(state.getPlan())) {
            if ("READY".equalsIgnoreCase(state.getPlan())) {
                StringFilter readinessFilter = criteria.readiness();
                readinessFilter.setContains("ready");
            } else if ("OFF_PLAN".equalsIgnoreCase(state.getPlan())) {
                PropertyCriteria.ListingTypeFilter listingTypeFilter = criteria.listingType();
                listingTypeFilter.setEquals(ListingType.OFF_PLAN);
            }
        }
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

    private String extractPropertyType(String message) {
        if (!StringUtils.hasText(message)) {
            return null;
        }
        Matcher matcher = PROPERTY_TYPE_PATTERN.matcher(message);
        if (!matcher.find()) {
            return null;
        }
        String token = matcher.group(1).toLowerCase(Locale.ENGLISH);
        return switch (token) {
            case "apt", "apts", "apartment", "flat" -> "APARTMENT";
            case "villa" -> "VILLA";
            case "townhouse" -> "TOWNHOUSE";
            case "penthouse" -> "PENTHOUSE";
            default -> null;
        };
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

    private void extractTimelinePreference(String message, AgentState state) {
        if (!StringUtils.hasText(message)) {
            return;
        }
        String lower = message.toLowerCase(Locale.ENGLISH);
        if (lower.contains("ready now") || lower.contains("ready property") || lower.contains("move in ready")) {
            state.setPlan("READY");
            return;
        }
        if (lower.contains("off-plan") || lower.contains("off plan")) {
            state.setPlan("OFF_PLAN");
        } else if (lower.contains("ready to move") || lower.contains("move-in ready") || lower.contains("handover")) {
            state.setPlan("READY");
        }
    }

    private void applyTimelinePreference(AgentState state, String timelineValue) {
        if (!StringUtils.hasText(timelineValue)) {
            return;
        }
        String normalized = timelineValue.trim().toUpperCase(Locale.ENGLISH);
        if ("READY".equals(normalized) || "READY_NOW".equals(normalized)) {
            state.setPlan("READY");
            return;
        }
        if ("OFF_PLAN".equals(normalized) || "OFFPLAN".equals(normalized)) {
            state.setPlan("OFF_PLAN");
            return;
        }
        try {
            int year = Integer.parseInt(timelineValue.trim());
            state.setCompletionYearFrom(year);
            state.setCompletionYearTo(year);
            if (year > LocalDate.now(ZoneOffset.UTC).getYear()) {
                state.setPlan("OFF_PLAN");
            }
        } catch (NumberFormatException ignored) {
            // Keep conversation flowing even if the model emits a free-form label.
        }
    }

    private String normalizePropertyType(String propertyType) {
        if (!StringUtils.hasText(propertyType)) {
            return null;
        }
        String normalized = propertyType.trim().toUpperCase(Locale.ENGLISH).replace(' ', '_');
        return switch (normalized) {
            case "APARTMENT", "FLAT" -> "APARTMENT";
            case "VILLA" -> "VILLA";
            case "TOWNHOUSE" -> "TOWNHOUSE";
            case "PENTHOUSE" -> "PENTHOUSE";
            default -> propertyType.trim().toUpperCase(Locale.ENGLISH);
        };
    }

    private String extractJsonCandidate(String aiResponse) {
        if (!StringUtils.hasText(aiResponse)) {
            return aiResponse;
        }
        String trimmed = aiResponse.trim();
        Matcher fencedMatcher = FENCED_JSON_PATTERN.matcher(trimmed);
        if (fencedMatcher.matches()) {
            return fencedMatcher.group(1).trim();
        }
        int firstBrace = trimmed.indexOf('{');
        int lastBrace = trimmed.lastIndexOf('}');
        if (firstBrace >= 0 && lastBrace > firstBrace) {
            return trimmed.substring(firstBrace, lastBrace + 1);
        }
        return trimmed;
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
        String timeline;
    }

    private record TurnRoutingDecision(
        String mode,
        boolean answerFirst,
        boolean useRag,
        boolean preferWebSearch,
        boolean showPropertyContext,
        boolean decisionSupport,
        boolean needsQualification,
        String reason
    ) {
        boolean allowEarlySearch() {
            return useRag || showPropertyContext;
        }

        boolean isExplorationOrInformation() {
            return answerFirst || decisionSupport || "DIRECT_ANSWER".equals(mode) || "WEB_SEARCH".equals(mode);
        }

        boolean isRecommendationOrProperty() {
            return showPropertyContext || useRag || "RAG_ANSWER".equals(mode);
        }

        boolean shouldRunWebSearch() {
            return preferWebSearch || "WEB_SEARCH".equals(mode);
        }
    }

    private record ConversationStepResult(String reply, List<PropertyContextDTO> context, AgentState state, boolean leadCreated) {
        static ConversationStepResult of(String reply, List<PropertyContextDTO> context, AgentState state, boolean leadCreated) {
            state.refreshStage();
            return new ConversationStepResult(reply, context, state, leadCreated);
        }
    }

    private record ConversationTurn(String role, String content) {}

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
        private String propertyType;

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
                state.propertyType = dto.getPropertyType();
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

        boolean hasLocationRequirement() {
            return StringUtils.hasText(city) || StringUtils.hasText(area);
        }

        boolean hasPropertyTypePreference() {
            return StringUtils.hasText(propertyType);
        }

        boolean hasTimelineRequirement() {
            return hasPlan() || completionYearFrom != null || completionYearTo != null;
        }

        boolean hasRecommendationRequirements() {
            return hasBudget() && hasPurpose() && hasLocationRequirement() && hasPropertyTypePreference() && hasTimelineRequirement();
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

        String getPropertyType() {
            return propertyType;
        }

        void setPropertyType(String propertyType) {
            this.propertyType = propertyType;
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
            dto.setPropertyType(propertyType);
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
