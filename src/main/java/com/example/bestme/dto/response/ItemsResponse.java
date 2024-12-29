package com.example.bestme.dto.response;

import com.example.bestme.domain.Item;
import org.springframework.data.domain.Slice;

import java.util.List;

public record ItemsResponse(
        List<ItemResponse> items,
        ItemSliceInfo sliceInfo
) {

    public static ItemsResponse from(Slice<Item> sliceItem) {
        List<ItemResponse> items = sliceItem.getContent().stream()
                .map(ItemResponse::from)
                .toList();
        ItemSliceInfo pagingInfo = ItemSliceInfo.from(sliceItem);

        return new ItemsResponse(items, pagingInfo);
    }

    public record ItemResponse(
            Long id,
            String imageUrl,
            String brandName,
            String itemName,
            int likeCount
    ) {

        public static ItemResponse from(Item item) {
            return new ItemResponse(
                    item.getId(),
                    item.getImageUrl(),
                    item.getBrand().getName(),
                    item.getName(),
                    item.getLikeCount()
            );
        }
    }

    public record ItemSliceInfo(
            int curPage,
            boolean hasNext
    ) {

        public static ItemSliceInfo from(Slice<Item> sliceItem) {
            return new ItemSliceInfo(
                    sliceItem.getNumber(),
                    sliceItem.hasNext()
            );
        }
    }
}
