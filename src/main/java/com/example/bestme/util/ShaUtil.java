package com.example.bestme.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ShaUtil {

    // 비밀번호를 SHA256 방식으로 암호화
    public static String sha256Encode(String text) {
        return new BCryptPasswordEncoder().encode(text);
    }
}
