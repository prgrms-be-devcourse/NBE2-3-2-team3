package com.example.bestme.repository;

import com.example.bestme.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = """
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
        AND (:brands IS NULL OR b.name IN (:brands))
        AND (:colors IS NULL OR co.name IN (:colors))
    """, nativeQuery = true)
    List<Item> findItemsBySearchCondition(Long categoryId, List<String> brands, List<String> colors);
}