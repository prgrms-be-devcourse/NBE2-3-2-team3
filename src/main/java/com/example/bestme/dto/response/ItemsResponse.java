package com.example.bestme.dto.response;

import com.example.bestme.domain.Item;

import java.util.List;

public record ItemsResponse(
        long totalCount,
        List<ItemResponse> items
) {

    public static ItemsResponse from(List<Item> items) {
        List<ItemResponse> itemResponses = items.stream()
                .map(ItemResponse::from)
                .toList();

        return new ItemsResponse(items.size(), itemResponses);
    }

    public record ItemResponse(
            Long id,
            String imageUrl,
            String itemName
    ) {

        public static ItemResponse from(Item item) {
            return new ItemResponse(
                    item.getId(),
                    item.getImageUrl(),
                    item.getName()
            );
        }
    }
}
