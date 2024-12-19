package com.example.bestme.dto.response;

import com.example.bestme.domain.Brand;
import com.example.bestme.domain.Color;

import java.util.List;

public record FilterMenuResponse(
        List<String> menu,
        List<BrandResponse> brands,
        List<ColorResponse> colors
) {

    public static FilterMenuResponse of(
            List<Brand> brands,
            List<Color> colors
    ) {
        return new FilterMenuResponse(
                List.of("brands", "colors"),
                brands.stream().map(BrandResponse::from).toList(),
                colors.stream().map(ColorResponse::from).toList()
        );
    }

    public record BrandResponse(Long id, String name) {

        public static BrandResponse from(Brand brand) {
            return new BrandResponse(brand.getId(), brand.getName());
        }
    }

    public record ColorResponse(Long id, String name) {

        public static ColorResponse from(Color color) {
            return new ColorResponse(color.getId(), color.getName());
        }
    }
}