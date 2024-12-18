package com.example.bestme.repository;

import com.example.bestme.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

//    @Query(
//            """
//            SELECT i FROM Item i
//            JOIN FETCH i.color co
//            JOIN FETCH i.category ca
//            WHERE (:categories IS NULL OR ca.name IN :categories)
//            AND(:colors IS NULL OR co.name IN :colors)
//            """
//    )
    @Query(
            """
            SELECT i FROM Item i
            JOIN FETCH i.itemCategories ic
            JOIN FETCH i.color co
            WHERE (:categories IS NULL OR ic.category.name IN :categories)
            AND(:colors IS NULL OR co.name IN :colors)
            """
    )
    List<Item> findItemsByFilterV1(List<String> categories, List<String> colors);

    @Query(value = """
        WITH RECURSIVE subcategories (id) AS (
            SELECT id
            FROM category
            WHERE :categoryId IS NULL OR id = :categoryId
    
            UNION ALL
    
            SELECT ca.id
            FROM category ca
            JOIN subcategories sc ON ca.parent_category_id = sc.id
        )
        SELECT DISTINCT i.*
        FROM item i
        JOIN item_category ic ON i.id = ic.item_id
        JOIN color co ON i.color_id = co.id
        WHERE (:categoryId IS NULL OR ic.category_id IN (SELECT id FROM subcategories))
        AND (:colors IS NULL OR co.name IN (:colors))
    """, nativeQuery = true)
    List<Item> findItemsByFilterV2(Long categoryId, List<String> colors);
}
