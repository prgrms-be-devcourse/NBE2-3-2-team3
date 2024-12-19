package com.example.bestme.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSignUpDTO {
    private String email;
    private String password;
    private String nickname;
}
