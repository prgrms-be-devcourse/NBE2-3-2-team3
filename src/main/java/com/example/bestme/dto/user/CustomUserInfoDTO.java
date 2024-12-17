package com.example.bestme.dto.user;

import com.example.bestme.domain.user.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomUserInfoDTO {
    private Long userId;
    private String email;
    private String nickname;
    private Role role;
}
