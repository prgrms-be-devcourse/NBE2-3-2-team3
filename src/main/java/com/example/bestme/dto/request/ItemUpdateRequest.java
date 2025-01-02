package com.example.bestme.dto.request;

public record ItemUpdateRequest(
        String name,
        String purchaseUrl,
        Long categoryId,
        Long colorId
) {
}
