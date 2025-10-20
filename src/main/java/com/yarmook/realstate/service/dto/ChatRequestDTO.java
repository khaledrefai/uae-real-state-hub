package com.yarmook.realstate.service.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatRequestDTO {

    @NotBlank
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
