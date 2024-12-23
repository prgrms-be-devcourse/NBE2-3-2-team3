package com.example.bestme.dto.response;

import com.example.bestme.domain.Color;

public record ColorSelectResponse(Long id, String name) {

    public static ColorSelectResponse from(Color color) {
        return new ColorSelectResponse(color.getId(), color.getName());
    }
}
