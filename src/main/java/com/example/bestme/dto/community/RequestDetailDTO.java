package com.example.bestme.dto.community;

import com.example.bestme.domain.community.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestDetailDTO {

    private Long boardId;
    private Long userId;
    private String subject;
    private String imagename;
    private String content;
    private Long view;

    @Enumerated(EnumType.STRING)
    private Category category;
}
