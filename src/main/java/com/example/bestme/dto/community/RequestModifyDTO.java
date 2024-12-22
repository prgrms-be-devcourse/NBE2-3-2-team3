package com.example.bestme.dto.community;

import com.example.bestme.domain.community.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestModifyDTO {

    private Long boardId;
    private Long userId;
    private String subject;
    private String imagename;
    private String content;

    @Schema(description = "카테고리", example = "BASIC")
    @Enumerated(EnumType.STRING)
    private Category category = Category.BASIC; // 기본값 설정
}
