package com.example.bestme.dto.api;

import lombok.*;

import java.time.LocalDateTime;


public class ResultResponse {

    @Getter
    @Setter
    public static class CreateResultResponseDTO {
        private Long resultId;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    public static class ReadResultResponseDTO {
        private Long colorId;
        private String lightestSkinColor;
        private String darkestSkinColor;
        private String lipColor;
        private String pupilColor;
        private String irisColor;
        private String hairColor;
        private LocalDateTime createdAt;
    }


    public interface ReadColorResponseDTO {
        Long getColorId();
        int getFrequency();
    }
}

