package com.example.bestme.domain;


import com.example.bestme.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id", nullable = false)
    private Long resultId;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 매핑
    private User user;


    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정
    @JoinColumn(name = "color_id", nullable = false) // 외래 키 매핑
    private Color color;


    @Column(name = "lightest_skin_color", nullable = false, length = 10)
    private String lightestSkinColor;

    @Column(name = "darkest_skin_color", nullable = false, length = 10)
    private String darkestSkinColor;

    @Column(name = "lip_color", nullable = false, length = 10)
    private String lipColor;

    @Column(name = "pupil_color", nullable = false, length = 10)
    private String pupilColor;

    @Column(name = "iris_color", nullable = false, length = 10)
    private String irisColor;

    @Column(name = "hair_color", nullable = false, length = 10)
    private String hairColor;
}
