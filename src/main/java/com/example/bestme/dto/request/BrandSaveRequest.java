package com.example.bestme.dto.request;

import com.example.bestme.domain.Brand;

public record BrandSaveRequest(
        String name,
        String homepageUrl
) {

    public Brand toEntity(String imageUrl) {
        return Brand.builder()
                .name(name)
                .homepageUrl(homepageUrl)
                .imageUrl(imageUrl)
                .build();
    }
}
