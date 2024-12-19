package com.example.bestme.dto.api;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatRequest {
    private String model;
    private List<Message> messages;
    private int max_tokens;
    private double temperature;

    public ChatRequest(String model, List<Message> messages, int maxTokens, double temperature) {
        this.model = model;
        this.messages = messages;
        this.max_tokens = maxTokens;
        this.temperature = temperature;
    }

}
