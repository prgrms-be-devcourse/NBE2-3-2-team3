package com.example.bestme.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user") // 테이블명 설정 (기본적으로 클래스명 소문자 복수형이 테이블명으로 매핑됨)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 설정
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "kakao_flag", nullable = false)
    private boolean kakaoFlag; // tinyint(1) -> boolean 타입

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_flag", nullable = false)
    private boolean deletedFlag; // tinyint(1) -> boolean 타입

    @Enumerated(EnumType.STRING)  // Enum을 문자열로 저장
    @Column(name = "role", nullable = false)
    private UserRole role;

    public enum UserRole {
        USER,
        ADMIN
    }

    public User(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }


}
