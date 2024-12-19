package com.example.bestme.dto.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


public class ResultRequest {

    @Getter
    public static class CreateResultDTO {
        private String lightestSkinColor;
        private String darkestSkinColor;
        private String lipColor;
        private String pupilColor;
        private String irisColor;
        private String hairColor;
    }
}

