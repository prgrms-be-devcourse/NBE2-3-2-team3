package com.example.bestme.repository;

import com.example.bestme.domain.ItemLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemLikeRepository extends JpaRepository<ItemLike, Long> {

    @Query(
            """
            SELECT il
            FROM ItemLike il
            WHERE il.item.id = :itemId AND il.user.userId = :userId
            """
    )
    Optional<ItemLike> findByItemIdAndUserId(Long itemId, Long userId);

    @Query(
            value = """
            DELETE FROM item_like
            WHERE item_id = :itemId AND user_id = :userId
            """,
            nativeQuery = true
    )
    void deleteByItemIdAndUserId(Long itemId, Long userId);

    @Query(
            value = """
                    INSERT INTO item_like (item_id, user_id) VALUES (:itemId, :userId)
                    """,
            nativeQuery = true
    )
    void insertItemLike(Long itemId, Long userId);
}
