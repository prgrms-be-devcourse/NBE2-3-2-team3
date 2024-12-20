package com.example.bestme.dto.response;

import com.example.bestme.domain.Category;

import java.util.List;

public record CategoryMenuResponse(
        List<CategorySelectResponse> categories
) {

    public static CategoryMenuResponse from(List<Category> categories) {
        List<CategorySelectResponse> categorySelectResponses = categories.stream()
                .map(CategorySelectResponse::from)
                .toList();

        return new CategoryMenuResponse(categorySelectResponses);
    }

    public record CategorySelectResponse(
            Long id,
            Integer depth,
            String parentCategoryName,
            String name,
            List<CategorySelectResponse> subCategories
    ) {

        public static CategorySelectResponse from(Category category) {
            List<CategorySelectResponse> subCategories = category.getSubCategories().stream()
                    .map(CategorySelectResponse::from)
                    .toList();

            return new CategorySelectResponse(
                    category.getId(),
                    category.getDepth(),
                    category.getParentCategoryName(),
                    category.getName(),
                    subCategories
            );
        }
    }
}