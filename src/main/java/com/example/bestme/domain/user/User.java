package com.example.bestme.domain.user;

import com.example.bestme.domain.Result;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean kakaoFlag;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean deletedFlag;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(Long userId, String email, String password, String nickname, String birth, Gender gender, boolean kakaoFlag, LocalDateTime createdAt, LocalDateTime deletedAt, boolean deletedFlag, Role role) {
        this.id = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
        this.kakaoFlag = kakaoFlag;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.deletedFlag = deletedFlag;
        this.role = role;
    }



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Result> results = new ArrayList<>();
}
