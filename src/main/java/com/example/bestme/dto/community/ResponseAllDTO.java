package com.example.bestme.dto.community;

import com.example.bestme.domain.community.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAllDTO {

    private Long boardId;
    private Long userId;
//    private String nickname;    // user 엔티티와 연결 후 사용
    private String subject;
    private Long view;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDateTime createdAt;
}
