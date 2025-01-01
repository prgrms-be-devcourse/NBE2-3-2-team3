package com.example.bestme.dto.community;

import com.example.bestme.domain.community.Category;
import com.example.bestme.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestWriteDTO implements BoardDTO {

    private Long userId;
    private User user;
//    private String nickname;    // user 엔티티와 연결 후 사용
//    private String password;    // user 엔티티와 연결 후 사용
    private String subject;
    private String imagename;
    private String content;

    @Schema(description = "카테고리", example = "BASIC")
    @Enumerated(EnumType.STRING)
    private Category category = Category.BASIC; // 기본값 설정

    @Override
    public void validate() {
        if (userId == null) throw new IllegalArgumentException("사용자 id가 없습니다.");
//        if (nickname == null) throw new IllegalArgumentException("닉네임을 입력해주세요.");                               // user 엔티티와 연결 후 사용
//        if (password == null || password.isBlank()) throw new IllegalArgumentException("비밀번호를 입력해주세요.");       // user 엔티티와 연결 후 사용
        if (subject == null || subject.isBlank()) throw new IllegalArgumentException("제목 입력은 필수입니다.");
        if (content == null || content.isBlank()) throw new IllegalArgumentException("내용 입력은 필수입니다.");
    }
}
