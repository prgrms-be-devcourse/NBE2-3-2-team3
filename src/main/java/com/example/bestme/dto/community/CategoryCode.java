package com.example.bestme.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum CategoryCode {

    // 카테고리
    SPRING("봄"),
    SUMMER("여름"),
    AUTUMN("가을"),
    WINTER("겨울"),
    BASIC("기본");

    private final String category;
}
