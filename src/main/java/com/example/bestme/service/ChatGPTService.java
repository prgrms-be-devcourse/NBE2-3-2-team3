package com.example.bestme.service;

import org.springframework.stereotype.Service;

@Service
public interface ChatGPTService {
    String getChatResponse(String userMessage);
}
