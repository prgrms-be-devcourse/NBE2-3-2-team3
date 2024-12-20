package com.example.bestme.controller.api;

import com.example.bestme.service.ChatGPTService;
import org.springframework.stereotype.Component;

@Component
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    public ChatGPTController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    public String chat(String userMessage) {
        return chatGPTService.getChatResponse(userMessage);
    }
}
