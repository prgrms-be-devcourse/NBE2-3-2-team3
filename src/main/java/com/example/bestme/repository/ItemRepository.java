package com.example.bestme.repository;

import com.example.bestme.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(
            """ 
            SELECT i FROM Item i
            JOIN FETCH i.color co
            JOIN FETCH i.category ca
            WHERE (:categories IS NULL OR ca.name IN :categories)
            AND(:colors IS NULL OR co.name IN :colors)
            """
    )
    List<Item> findItemsByFilter(List<String> categories, List<String> colors);

    @Query(
            """ 
            SELECT COUNT(i) FROM Item i
            JOIN i.color co
            JOIN i.category ca
            WHERE (:categories IS NULL OR ca.name IN :categories)
            AND(:colors IS NULL OR co.name IN :colors)
            """
    )
    long findItemCount(List<String> categories, List<String> colors);
}
