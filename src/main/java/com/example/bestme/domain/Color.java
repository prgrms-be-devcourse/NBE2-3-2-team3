package com.example.bestme.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.jcip.annotations.Immutable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Immutable // 읽기 전용 설정
public class Color extends BaseEntity {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "color", fetch = FetchType.LAZY) // 1:N 관계 양방향 매핑
    private List<Result> results = new ArrayList<>();

    @OneToMany(mappedBy = "color", fetch = FetchType.LAZY) // 1:N 관계 양방향 매핑
    private List<Guide> guides = new ArrayList<>();

    public Color(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
