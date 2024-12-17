package com.example.bestme.dto;

import lombok.*;

import java.time.LocalDateTime;


public class ResultResponse {

    @Getter
    @Setter
    public static class CreateResultResponseDTO {
        private Long resultId;
        private LocalDateTime createdAt;
    }
}

