package com.example.bestme.repository;

import com.example.bestme.domain.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.NoSuchElementException;

public interface ItemRepository extends JpaRepository<Item, Long> {

    default Item getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("아이템을 찾을 수 없음"));
    }

    @Query(
            value = """
                    WITH RECURSIVE cte (id) AS (
                        SELECT id
                        FROM category
                        WHERE :categoryId IS NULL OR id = :categoryId
                    
                        UNION ALL
                    
                        SELECT ca.id
                        FROM category ca
                        JOIN cte ON ca.parent_category_id = cte.id
                    )
                    SELECT DISTINCT i.*
                    FROM item i
                    JOIN brand b ON i.brand_id = b.id
                    JOIN color co ON i.color_id = co.id
                    WHERE (:categoryId IS NULL OR i.category_id IN (SELECT id FROM cte))
                    AND (COALESCE(:brands) IS NULL OR b.name IN (:brands))
                    AND (COALESCE(:colors) IS NULL OR co.name IN (:colors))
                    """,
            countQuery = """
                    WITH RECURSIVE cte (id) AS (
                                SELECT id
                                FROM category
                                WHERE :categoryId IS NULL OR id = :categoryId
                    
                                UNION ALL
                    
                                SELECT ca.id
                                FROM category ca
                                JOIN cte ON ca.parent_category_id = cte.id
                            )
                            SELECT COUNT(DISTINCT i.id)
                            FROM item i
                            JOIN brand b ON i.brand_id = b.id
                            JOIN color co ON i.color_id = co.id
                            WHERE (:categoryId IS NULL OR i.category_id IN (SELECT id FROM cte))
                            AND (COALESCE(:brands) IS NULL OR b.name IN (:brands))
                            AND (COALESCE(:colors) IS NULL OR co.name IN (:colors))
                    """
            , nativeQuery = true)
    Slice<Item> findSliceItemsBySearchCondition(Long categoryId, List<String> brands, List<String> colors, Pageable pageable);

    List<Item> findTop3ByCategoryIdOrderByLikeCountDesc(Long categoryId);

    @Modifying(clearAutomatically = true)
    @Query(
            value = """
                    UPDATE item
                    SET like_count = like_count + 1
                    WHERE id = :itemId
                    """,
            nativeQuery = true
    )
    void increaseLike(Long itemId);

    @Modifying(clearAutomatically = true)
    @Query(
            value = """
                    UPDATE item
                    SET like_count = like_count - 1
                    WHERE id = :itemId
                    """,
            nativeQuery = true
    )
    void decreaseLike(Long itemId);
}