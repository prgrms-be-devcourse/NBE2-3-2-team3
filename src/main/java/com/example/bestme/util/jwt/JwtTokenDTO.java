package com.example.bestme.util.jwt;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}

