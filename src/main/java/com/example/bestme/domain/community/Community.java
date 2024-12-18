package com.example.bestme.domain.community;

import com.example.bestme.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity( name = "board" )
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Community extends BaseEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "board_id" )
    private Long boardId;

    /* user 테이블과 연관관계 매핑 시 적용하기
    // Community 엔티티 설정
    // FetchType.LAZY: 연관된 데이터를 지연 로딩합니다. (UserEntity는 필요할 때만 로드)
    // referencedColumnName : 참조 대상 엔티티의 PK가 아닌 다른 컬럼을 외래 키로 참조해야 할 때 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "user_id", nullable = false)    // FK 매핑
    private User user;

    // User 엔티티 설정 ( 양방향으로 연결할 경우 )
    // FetchType.LAZY (기본값): 연관된 데이터가 필요할 때만 로드됩니다.
    // CascadeType.ALL: 부모 엔티티의 변경 사항(저장, 삭제 등)이 자식 엔티티에 전파됩니다.
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore     // Json 변환 과정에서 무한 루프 방지
    private List<Community> boards = new ArrayList<>();
     */

    @Column( name = "user_id", nullable = false )
    private Long userId;

    @Column( length = 10, nullable = false )
    private String subject;

    private String imagename;

    @Column( columnDefinition = "TEXT", nullable = false )
    private String content;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long view;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'BASIC'")   // enum(문자열)을 기본값으로 지정 시, 작은 따음표 추가 필요
    private Category category;

    @PrePersist
    protected void prePersist() {
        if (this.category == null) {
            this.category = Category.BASIC;
        }
        if (this.view == null) {
            this.view = 0L;
        }
    }
}
