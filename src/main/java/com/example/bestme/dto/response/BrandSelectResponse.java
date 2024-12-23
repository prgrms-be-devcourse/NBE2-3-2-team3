package com.example.bestme.dto.response;

import com.example.bestme.domain.Brand;

public record BrandSelectResponse(Long id, String name) {

    public static BrandSelectResponse from(Brand brand) {
        return new BrandSelectResponse(brand.getId(), brand.getName());
    }
}
