package com.example.bestme.dto.user;

import com.example.bestme.domain.user.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestModifyUserDTO {
    private String originPassword;
    private String nickname;
    private String birth;
    private Gender gender;
    private String newPassword;
}
