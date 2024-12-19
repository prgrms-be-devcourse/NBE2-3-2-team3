package com.example.bestme.dto.response;


import com.example.bestme.domain.Brand;
import com.example.bestme.domain.Category;
import com.example.bestme.domain.Color;

import java.util.List;

public record FilterMenuResponse(
        List<String> menu,
        List<CategoryResponse> categories,
        List<BrandResponse> brands,
        List<ColorResponse> colors
) {

    public static FilterMenuResponse of(
            List<Category> categories,
            List<Brand> brands,
            List<Color> colors
    ) {
        return new FilterMenuResponse(
                List.of("categories", "colors"),
                categories.stream().map(CategoryResponse::from).toList(),
                brands.stream().map(BrandResponse::from).toList(),
                colors.stream().map(ColorResponse::from).toList()
        );
    }

    public record CategoryResponse(
            Long id,
            Integer depth,
            String parentCategoryName,
            String name,
            List<CategoryResponse> subCategories
    ) {

        public static CategoryResponse from(Category category) {
            List<CategoryResponse> subCategories = category.getSubCategories().stream()
                    .map(CategoryResponse::from)
                    .toList();

            return new CategoryResponse(
                    category.getId(),
                    category.getDepth(),
                    category.getParentCategoryName(),
                    category.getName(),
                    subCategories
            );
        }
    }

    public record BrandResponse(
            Long id,
            String imageUrl,
            String name,
            String homepageUrl
    ) {

        public static BrandResponse from(Brand brand) {
            return new BrandResponse(
                    brand.getId(),
                    brand.getImageUrl(),
                    brand.getName(),
                    brand.getHomepageUrl()
            );
        }
    }

    public record ColorResponse(Long id, String name) {

        public static ColorResponse from(Color color) {
            return new ColorResponse(color.getId(), color.getName());
        }
    }
}
