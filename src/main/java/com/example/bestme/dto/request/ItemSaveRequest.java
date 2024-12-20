package com.example.bestme.dto.request;

import com.example.bestme.domain.Brand;
import com.example.bestme.domain.Category;
import com.example.bestme.domain.Color;
import com.example.bestme.domain.Item;

public record ItemSaveRequest(
        String name,
        String purchaseUrl,
        Long colorId,
        Long categoryId,
        Long brandId
) {
    
    public Item toEntity(Color color, Category category, Brand brand, String imageUrl) {
        return Item.builder()
                .name(name)
                .purchaseUrl(purchaseUrl)
                .imageUrl(imageUrl)
                .color(color)
                .category(category)
                .brand(brand)
                .build();
    }
}
