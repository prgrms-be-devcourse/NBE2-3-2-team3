package com.example.bestme.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestLoginDTO {
    private String email;
    private String password;
}
