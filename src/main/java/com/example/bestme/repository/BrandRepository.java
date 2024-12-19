package com.example.bestme.repository;

import com.example.bestme.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

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
        SELECT DISTINCT b.*
        FROM brand b
        JOIN item i ON b.id = i.brand_id
        WHERE (:categoryId IS NULL OR i.category_id IN (SELECT id FROM cte))
    """, nativeQuery = true)
    List<Brand> findAllByCategoryId(Long categoryId);
}
