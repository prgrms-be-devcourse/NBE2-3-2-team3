package com.example.bestme.repository;

import com.example.bestme.domain.ItemLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemLikeRepository extends JpaRepository<ItemLike, Long> {

    Optional<ItemLike> findByItemIdAndUserId(Long itemId, Long userId);

    void deleteByItemIdAndUserId(Long itemId, Long userId);

    boolean existsByItemIdAndUserId(Long itemId, Long userId);
}
