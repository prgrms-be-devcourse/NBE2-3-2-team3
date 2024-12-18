package com.example.bestme.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLoginDTO {
    private String email;
    private String password;
}
