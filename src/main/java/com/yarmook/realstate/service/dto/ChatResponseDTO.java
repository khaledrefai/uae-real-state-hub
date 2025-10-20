package com.yarmook.realstate.service.dto;

import java.util.ArrayList;
import java.util.List;

public class ChatResponseDTO {

    private String answer;
    private List<PropertyContextDTO> context = new ArrayList<>();

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<PropertyContextDTO> getContext() {
        return context;
    }

    public void setContext(List<PropertyContextDTO> context) {
        this.context = context;
    }
}
