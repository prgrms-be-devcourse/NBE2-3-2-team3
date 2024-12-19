package com.example.bestme.dto.request;

import java.util.List;

public record SearchConditionRequest(
        Long categoryId,
        List<String> brands,
        List<String> colors
) {

    public static SearchConditionRequest of(
            Long categoryId,
            List<String> brands,
            List<String> colors
    ) {
        return new SearchConditionRequest(categoryId, brands, colors);
    }
}
