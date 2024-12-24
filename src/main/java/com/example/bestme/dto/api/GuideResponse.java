package com.example.bestme.dto.api;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class GuideResponse {

    @Getter
    @Setter
    public static class ReadGuideResponseDTO {
        private Long guideId;
        private Long colorId;
        private Long categoryId;
        private String description;
    }

    @Getter
    @Setter
    public static class CreateGuideResponseDTO {
        private Long guideId;
        private Long colorId;
        private Long categoryId;
        private LocalDateTime createdAt;
    }
}

