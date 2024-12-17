package com.example.bestme.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean kakaoFlag;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean deletedFlag;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(Long userId, String email, String password, String nickname, boolean kakaoFlag, LocalDateTime createdAt, LocalDateTime deletedAt, boolean deletedFlag, Role role) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.kakaoFlag = kakaoFlag;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.deletedFlag = deletedFlag;
        this.role = role;
    }
}
