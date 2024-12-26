package com.example.bestme.service;

import com.example.bestme.domain.Item;
import com.example.bestme.domain.ItemLike;
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

    public LikeResponse clickItemLike(Long itemId, Long userId) {
        Item item = itemRepository.getById(itemId);
        Optional<ItemLike> itemLike = itemLikeRepository.findByItemIdAndUserId(itemId, userId);

        int likeCount = item.getLikeCount();
        boolean isLiked = true;
        if (itemLike.isPresent()) {
            itemRepository.decreaseLike(itemId);
            itemLikeRepository.deleteByItemIdAndUserId(itemId, userId);
            likeCount--;
            isLiked = false;
        } else {
            itemRepository.increaseLike(itemId);
            itemLikeRepository.insertItemLike(itemId, userId);
            likeCount++;
        }

        return LikeResponse.of(likeCount, isLiked);
    }
}
