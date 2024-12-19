package com.example.bestme.repository;

import com.example.bestme.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(
            """
            SELECT ca FROM Category ca
            WHERE ca.parentCategory IS NULL
            """
    )
    List<Category> findAllByParentCategoryIdIsNull();
}
