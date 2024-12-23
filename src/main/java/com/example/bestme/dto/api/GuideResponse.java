package com.example.bestme.dto.api;

import lombok.Getter;
import lombok.Setter;


public class GuideResponse {

    @Getter
    @Setter
    public static class ReadGuideResponseDTO {
        private Long guideId;
        private Long colorId;
        private Long categoryId;
        private String description;
    }
}

