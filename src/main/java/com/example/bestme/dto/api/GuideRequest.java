package com.example.bestme.dto.api;

import lombok.Getter;


public class GuideRequest {

    @Getter
    public static class CreateGuideDTO {
        private Long color;
        private Long category;
        private String description;
    }
}

