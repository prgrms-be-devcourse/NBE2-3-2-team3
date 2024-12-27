package com.example.bestme.dto.response;

import com.example.bestme.domain.Item;

public record ItemDetailResponse(
        Long id,
        String name,
        int likeCount,
        String imageUrl,
        String purchaseUrl,
        String brandImageUrl,
        String brandHomepageUrl,
        String brandName,
        String colorName
) {

    public static ItemDetailResponse from(Item item) {
        return new ItemDetailResponse(
                item.getId(),
                item.getName(),
                item.getLikeCount(),
                item.getImageUrl(),
                item.getPurchaseUrl(),
                item.getBrand().getImageUrl(),
                item.getBrand().getHomepageUrl(),
                item.getBrand().getName(),
                item.getColor().getName()
        );
    }
}
