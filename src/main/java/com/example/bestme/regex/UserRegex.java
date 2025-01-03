package com.example.bestme.regex;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserRegex {

    // 이메일 정규식 설명
    // 1번 그룹 - 숫자 영소문자 영대문자 특수문자 . 특수문자 _ 로 이루어진 그룹 2 ~ 20자
    // 2번 그룹 - 특수문자 @
    // 3번 그룹 - 숫자 영소문자 특수문자 - 로 이루어진 그룹 2 ~ 20자 <- 숫자 or 영소문자가 최소 1 ~ 20자 포함됨
    // 4번 그룹 - 특수문자 .
    // 5번 그룹 - 영소문자로 이루어진 그룹 2 ~ 10 자
    public static final Regex email = new Regex("^([\\da-zA-Z_.]{2,20})@([\\da-z\\-]{2,20})?([\\da-z]{1,20})\\.([a-z]{2,10})$");

    // 닉네임 정규식 설명
    // 숫자, 영소문자, 영대문자, 한글, 특수문자 -, 특수문자 _ 로 이루어진 그룹 2 ~ 8자
    public static final Regex nickname = new Regex("^([\\da-zA-Z가-힣\\-_]{2,8})$");

    // 비밀번호 설명
    // 영대소문자, 숫자, 각종 특수문자를 1개 이상 포함하고 이들로 이루어진 그룹 8 ~ 20자
    public static final Regex password = new Regex("^(?=.*[a-zA-Z])(?=.*[\\d])(?=.*[`!@#$%^&*()\\-_=+\\[{\\]};:])[a-zA-Z\\d`!@#$%^&*()\\-_=+\\[{\\]};:]{8,20}$");

    // 주민번호 설명 (앞자리)
    // 숫자 로 이루어진 6자
    public static final Regex birth = new Regex("^[\\d]{6}$");
}
