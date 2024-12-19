package com.example.bestme.service.impl;



import com.example.bestme.dto.api.ChatRequest;
import com.example.bestme.dto.api.ChatResponse;
import com.example.bestme.dto.api.Message;
import com.example.bestme.service.ChatGPTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;

@Slf4j
@Service
public class ChatGPTServiceImpl implements ChatGPTService {

    private final WebClient webClient;

    @Value("${chatgpt.model}")
    private String modelName;

    public ChatGPTServiceImpl(WebClient chatGPTWebClient) {
        this.webClient = chatGPTWebClient;
    }



    @Override
    public String getChatResponse(String userMessage) {
        ChatRequest request = new ChatRequest(
                modelName,
                List.of(
                        new Message("system", "You are a helpful assistant."),
                        new Message("user", userMessage)
                ),
                100,
                0.7
        );

        ChatResponse response = webClient.post()
                .header("Content-Type", "application/json")  // Content-Type 추가
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        return response.getChoices().get(0).getMessage().getContent();
    }

}