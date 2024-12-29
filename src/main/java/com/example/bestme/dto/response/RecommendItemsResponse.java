package com.example.bestme.dto.response;

import com.example.bestme.domain.Category;
import com.example.bestme.domain.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record RecommendItemsResponse(
        List<String> category,
        Map<String, List<ItemResponse>> items
) {

    public static RecommendItemsResponse of(
            List<Category> categories,
            Map<String, List<Item>> items
    ) {
        List<String> categoryNames = categories.stream()
                .map(Category::getName)
                .toList();
        Map<String, List<ItemResponse>> itemResponses = new HashMap<>();

        for (String categoryName : categoryNames) {
            itemResponses.put(
                    categoryName,
                    items.get(categoryName).stream()
                            .map(ItemResponse::from)
                            .toList()
            );
        }

        return new RecommendItemsResponse(categoryNames, itemResponses);
    }

    public record ItemResponse(
            Long id,
            String imageUrl,
            String brandName,
            String itemName,
            String purchaseUrl,
            int likeCount
    ) {

        public static ItemResponse from(Item item) {
            return new ItemResponse(
                    item.getId(),
                    item.getImageUrl(),
                    item.getBrand().getName(),
                    item.getName(),
                    item.getPurchaseUrl(),
                    item.getLikeCount()
            );
        }
    }
}
