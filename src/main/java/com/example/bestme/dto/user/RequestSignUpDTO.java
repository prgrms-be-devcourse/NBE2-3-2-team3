package com.example.bestme.dto.user;

import com.example.bestme.domain.user.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSignUpDTO {
    private String email;
    private String password;
    private String nickname;
    private String birth;
    private Gender gender;
}
