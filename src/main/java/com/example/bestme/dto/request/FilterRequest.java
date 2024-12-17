package com.example.bestme.dto.request;

import java.util.List;

public record FilterRequest(
        List<String> categories,
        List<String> colors
) {

    public static FilterRequest of(List<String> categories, List<String> colors) {
        return new FilterRequest(categories, colors);
    }
}
