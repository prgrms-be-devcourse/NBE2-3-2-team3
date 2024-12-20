package com.example.bestme.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public interface ChatGPTService {
    String getChatResponse(String userMessage) throws WebClientResponseException;
}
