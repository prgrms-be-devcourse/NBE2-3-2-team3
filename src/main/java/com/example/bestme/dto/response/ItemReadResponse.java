package com.example.bestme.dto.response;

import com.example.bestme.domain.Item;

public record ItemReadResponse(
        String name,
        String purchaseUrl,
        String categoryName,
        String colorName
) {

    public static ItemReadResponse from(Item item) {
        return new ItemReadResponse(
                item.getName(),
                item.getPurchaseUrl(),
                item.getCategory().getName(),
                item.getColor().getName()
        );
    }
}
