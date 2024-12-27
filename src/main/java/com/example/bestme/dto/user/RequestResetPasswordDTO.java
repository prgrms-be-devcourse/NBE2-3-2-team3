package com.example.bestme.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestResetPasswordDTO {
    private Long userId;
    private String password;
}
