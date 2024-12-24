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
public class RequestDeleteDTO implements BoardDTO {

    private Long boardId;
    private Long userId;
//    private String nickname;    // user 엔티티와 연결 후 사용
//    private String password;    // user 엔티티와 연결 후 사용

    @Override
    public void validate() {
        if (boardId == null) throw new IllegalArgumentException("게시물 id가 없습니다.");
        if (userId == null) throw new IllegalArgumentException("사용자 id가 없습니다.");
//        if (nickname == null) throw new IllegalArgumentException("닉네임을 입력해주세요.");       // user 엔티티와 연결 후 사용
//        if (password == null || password.isBlank()) throw new IllegalArgumentException("비밀번호를 입력해주세요.");       // user 엔티티와 연결 후 사용
    }
}
