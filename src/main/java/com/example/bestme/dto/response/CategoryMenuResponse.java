package com.example.bestme.dto.response;

import com.example.bestme.domain.Category;

import java.util.List;

public record CategoryMenuResponse(
        List<CategoryMenu> categoryMenu
) {

    public static CategoryMenuResponse from(List<Category> categories) {
        List<CategoryMenu> categoryMenus = categories.stream()
                .map(CategoryMenu::from)
                .toList();

        return new CategoryMenuResponse(categoryMenus);
    }

    public record CategoryMenu(
            Long id,
            Integer depth,
            String parentCategoryName,
            String name,
            List<CategoryMenu> subCategories
    ) {

        public static CategoryMenu from(Category category) {
            List<CategoryMenu> subCategories = category.getSubCategories().stream()
                    .map(CategoryMenu::from)
                    .toList();

            return new CategoryMenu(
                    category.getId(),
                    category.getDepth(),
                    category.getParentCategoryName(),
                    category.getName(),
                    subCategories
            );
        }
    }
}