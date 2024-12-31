package com.example.bestme.service;

import com.example.bestme.domain.Item;
import com.example.bestme.domain.ItemLike;
import com.example.bestme.domain.user.User;
import com.example.bestme.dto.response.LikeResponse;
import com.example.bestme.repository.ItemLikeRepository;
import com.example.bestme.repository.ItemRepository;
import com.example.bestme.repository.user.UserRepository;
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
    private final UserRepository userRepository;

    public LikeResponse clickItemLike(Long itemId, Long userId) {
        Item item = itemRepository.getById(itemId);
        User user = userRepository.getById(userId);

        int likeCount = getAfterClickLikeCount(item, user);
        boolean isLiked = itemLikeRepository.existsByItemIdAndUserId(itemId, userId);

        return LikeResponse.of(likeCount, isLiked);
    }

    private int getAfterClickLikeCount(Item item, User user) {
        Optional<ItemLike> itemLike = itemLikeRepository.findByItemIdAndUserId(item.getId(), user.getId());
        if (itemLike.isPresent()) {
            itemRepository.decreaseLike(item.getId());
            itemLikeRepository.deleteByItemIdAndUserId(item.getId(), user.getId());
            return item.getLikeCount() - 1;
        }

        itemRepository.increaseLike(item.getId());
        itemLikeRepository.save(
                ItemLike.builder()
                        .item(item)
                        .user(user)
                        .build());
        return item.getLikeCount() + 1;
    }
}
