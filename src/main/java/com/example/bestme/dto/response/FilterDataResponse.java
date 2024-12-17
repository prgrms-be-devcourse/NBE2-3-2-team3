package com.example.bestme.dto.response;

import com.example.bestme.domain.Category;
import com.example.bestme.domain.Color;

import java.util.List;

public record FilterDataResponse(
        List<String> menu,
        List<CategoryResponse> categories,
        List<ColorResponse> colors
) {

    public static FilterDataResponse of(
            List<Category> categories,
            List<Color> colors
    ) {
        return new FilterDataResponse(
                List.of("categories", "colors"),
                categories.stream().map(CategoryResponse::from).toList(),
                colors.stream().map(ColorResponse::from).toList()
        );
    }

    public record ColorResponse(Long id, String name) {

        public static ColorResponse from(Color color) {
            return new ColorResponse(color.getId(), color.getName());
        }
    }

    public record CategoryResponse(Long id, String name) {

        public static CategoryResponse from(Category category) {
            return new CategoryResponse(category.getId(), category.getName());
        }
    }
}
