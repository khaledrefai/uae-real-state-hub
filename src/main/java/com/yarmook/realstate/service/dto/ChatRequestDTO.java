package com.yarmook.realstate.service.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatRequestDTO {

    @NotBlank
    private String message;

    private AiAgentStateDTO agentState;

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
}
