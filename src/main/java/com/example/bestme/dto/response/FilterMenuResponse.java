package com.example.bestme.dto.response;

import com.example.bestme.domain.Brand;
import com.example.bestme.domain.Color;

import java.util.List;

public record FilterMenuResponse(
        List<String> menu,
        List<BrandSelectResponse> brands,
        List<ColorSelectResponse> colors
) {

    public static FilterMenuResponse of(
            List<Brand> brands,
            List<Color> colors
    ) {
        return new FilterMenuResponse(
                List.of("brands", "colors"),
                brands.stream().map(BrandSelectResponse::from).toList(),
                colors.stream().map(ColorSelectResponse::from).toList()
        );
    }
}