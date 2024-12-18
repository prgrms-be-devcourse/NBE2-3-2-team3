package com.example.bestme.dto.community;

import com.example.bestme.domain.community.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
public class CommunityDTO {

    private Long boardId;
    private Long userId;
    private String subject;
    private String imagename;
    private String content;
    private Long view = 0L; // 기본값 설정

    @Enumerated(EnumType.STRING)
    private Category category; // 기본값 설정
}
