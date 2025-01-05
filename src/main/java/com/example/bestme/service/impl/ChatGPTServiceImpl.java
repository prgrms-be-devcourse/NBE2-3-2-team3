package com.example.bestme.service.impl;



import com.example.bestme.dto.api.ChatRequest;
import com.example.bestme.dto.api.ChatResponse;
import com.example.bestme.dto.api.Message;
import com.example.bestme.service.ChatGPTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


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
    public String getChatResponse(String userMessage) throws WebClientResponseException {
        ChatRequest request = new ChatRequest(
                modelName,
                List.of(
                        new Message("user", userMessage)
                ),
                300,
                0.2
        );

        try {
            // API 요청 및 응답 처리
            ChatResponse response = webClient.post()
                    .header("Content-Type", "application/json") // Content-Type 추가
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .block();

            // 정상 응답 반환
            return response.getChoices().get(0).getMessage().getContent();

        } catch (WebClientResponseException e) {
            // HTTP 상태 코드 기반 예외 처리
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                // 401 Unauthorized - 인증 키 만료
                log.error("인증 오류: API 키가 만료되었거나 유효하지 않습니다. 상태 코드: {}", e.getStatusCode());
                throw new IllegalStateException("인증 키가 만료되었거나 유효하지 않습니다. 새로운 키로 다시 시도해주세요.", e);

            } else if (e.getStatusCode().is4xxClientError()) {
                // 기타 4xx 오류
                log.error("클라이언트 오류: {}", e.getMessage(), e);
                throw new IllegalArgumentException("잘못된 요청입니다. 상세 메시지: " + e.getResponseBodyAsString(), e);

            } else if (e.getStatusCode().is5xxServerError()) {
                // 5xx 서버 오류
                log.error("서버 오류: {}", e.getMessage(), e);
                throw new IllegalStateException("서버에 문제가 발생했습니다. 나중에 다시 시도해주세요.", e);

            } else {
                // 알 수 없는 HTTP 오류
                log.error("알 수 없는 오류 발생: {}", e.getMessage(), e);
                throw new IllegalStateException("알 수 없는 오류가 발생했습니다. 상태 코드: " + e.getStatusCode(), e);
            }

        }
    }

}