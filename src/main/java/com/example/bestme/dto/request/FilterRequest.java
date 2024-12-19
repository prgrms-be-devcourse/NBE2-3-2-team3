package com.example.bestme.dto.request;

import java.util.List;

public record FilterRequest(
        Long categoryId,
        List<String> brands,
        List<String> colors
) {

    public static FilterRequest of(
            Long categoryId,
            List<String> brands,
            List<String> colors
    ) {
        return new FilterRequest(categoryId, brands, colors);
    }
}
