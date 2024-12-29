package com.example.bestme.dto.user;

import com.example.bestme.domain.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseUserDTO {
    private Long id;
    private String nickname;
    private String birth;
    private Gender gender;
}
