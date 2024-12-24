package com.example.bestme.dto.response;

import com.example.bestme.domain.Item;
import org.springframework.data.domain.Page;

import java.util.List;

public record ItemsResponse(
        List<ItemResponse> items,
        ItemPagingInfo pagingInfo
) {

    public static ItemsResponse from(Page<Item> pagingItem) {
        List<ItemResponse> items = pagingItem.getContent().stream()
                .map(ItemResponse::from)
                .toList();
        ItemPagingInfo pagingInfo = ItemPagingInfo.from(pagingItem);

        return new ItemsResponse(items, pagingInfo);
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

    public record ItemPagingInfo(
            int curPage,
            int totalPages,
            boolean hasPrev,
            boolean hasNext,
            boolean isFirst,
            boolean isLast
    ) {

        public static ItemPagingInfo from(Page<Item> pagingItem) {
            return new ItemPagingInfo(
                    pagingItem.getNumber(),
                    pagingItem.getTotalPages(),
                    pagingItem.hasPrevious(),
                    pagingItem.hasNext(),
                    pagingItem.isFirst(),
                    pagingItem.isLast()
            );
        }
    }
}
