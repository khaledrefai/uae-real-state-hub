package com.yarmook.realstate.service.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ChatRequestDTO {

    @NotBlank
    private String message;

    private AiAgentStateDTO agentState;
    private List<ConversationHistoryMessageDTO> conversationHistory;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AiAgentStateDTO getAgentState() {
        return agentState;
    }

    public void setAgentState(AiAgentStateDTO agentState) {
        this.agentState = agentState;
    }

    public List<ConversationHistoryMessageDTO> getConversationHistory() {
        return conversationHistory;
    }

    public void setConversationHistory(List<ConversationHistoryMessageDTO> conversationHistory) {
        this.conversationHistory = conversationHistory;
    }

    public static class ConversationHistoryMessageDTO {

        private String role;
        private String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
