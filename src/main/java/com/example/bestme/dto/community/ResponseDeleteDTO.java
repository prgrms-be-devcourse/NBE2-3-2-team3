package com.example.bestme.dto.community;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDeleteDTO {

    private String nickname;    // user 엔티티와 연결 후 사용
    private String subject;
}
