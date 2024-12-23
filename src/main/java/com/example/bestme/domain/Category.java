package com.example.bestme.domain;

import com.example.bestme.domain.user.Guide;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> subCategories = new ArrayList<>();

    public String getParentCategoryName() {
        if (parentCategory != null) {
            return parentCategory.getName();
        }
        return "root";
    }

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY) // 1:N 관계 양방향 매핑
    private List<Guide> guides = new ArrayList<>();
}