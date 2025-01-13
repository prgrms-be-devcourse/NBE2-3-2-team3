package com.example.bestme.service;

import com.example.bestme.domain.Item;
import com.example.bestme.domain.ItemLike;
import com.example.bestme.domain.user.User;
import com.example.bestme.dto.response.LikeResponse;
import com.example.bestme.repository.ItemLikeRepository;
import com.example.bestme.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final ItemRepository itemRepository;
    private final ItemLikeRepository itemLikeRepository;

    public LikeResponse getLikeResponse(Long itemId, Long userId) {
        clickItemLike(itemId, userId);
        Item item = itemRepository.getById(itemId);
        boolean isLiked = !itemLikeRepository.existsByItemIdAndUserId(itemId, userId);

        return LikeResponse.of(item.getLikeCount(), isLiked);
    }

    private void clickItemLike(Long itemId, Long userId) {
        Optional<ItemLike> itemLike = itemLikeRepository.findByItemIdAndUserId(itemId, userId);
        if (itemLike.isPresent()) {
            itemRepository.decreaseLike(itemId);
            itemLikeRepository.deleteByItemIdAndUserId(itemId, userId);
        }
        itemRepository.increaseLike(itemId);
        itemLikeRepository.save(
                ItemLike.builder()
                        .item(Item.builder().id(itemId).build())
                        .user(User.builder().id(userId).build())
                        .build());
    }
}
