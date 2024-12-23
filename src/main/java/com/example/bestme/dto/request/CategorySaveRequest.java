package com.example.bestme.dto.request;

import com.example.bestme.domain.Category;

public record CategorySaveRequest(
        String name,
        String parentCategoryId
) {

    public Category toEntity(Category parentCategory) {
        return Category.builder()
                .name(name)
                .parentCategory(parentCategory)
                .build();
    }
}
