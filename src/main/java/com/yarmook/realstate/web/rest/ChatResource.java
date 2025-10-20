package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.service.ai.RealEstateChatService;
import com.yarmook.realstate.service.dto.ChatRequestDTO;
import com.yarmook.realstate.service.dto.ChatResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatResource {

    private final RealEstateChatService chatService;

    public ChatResource(RealEstateChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDTO> chat(@Valid @RequestBody ChatRequestDTO requestDTO) {
        return ResponseEntity.ok(chatService.chat(requestDTO));
    }
}
