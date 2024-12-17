package com.example.bestme.domain.community;

import com.example.bestme.domain.BaseEntity;
import com.example.bestme.dto.community.CategoryCode;
import jakarta.persistence.*;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity( name = "board" )
@Setter
@ToString
public class CommunityEntity extends BaseEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(columnDefinition = "BIGINT")
    private Long board_id;

    // user 테이블과 연관관계 매핑 필요
    // FetchType.LAZY: 연관된 데이터를 지연 로딩합니다. (UserEntity는 필요할 때만 로드)
    // referencedColumnName : 참조 대상 엔티티의 PK가 아닌 다른 컬럼을 외래 키로 참조해야 할 때 사용
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn( name = "user_id", nullable = false)
//    private UserEntity userId;

    // UserEntity에서 설정해줘야 하는 것
    // FetchType.LAZY (기본값): 연관된 데이터가 필요할 때만 로드됩니다.
    //CascadeType.ALL: 부모 엔티티의 변경 사항(저장, 삭제 등)이 자식 엔티티에 전파됩니다.
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<CommunityEntity> boards = new ArrayList<>();

    @Column(columnDefinition = "BIGINT NOT NULL")
    private Long user_id;

    @Column(nullable = false, length = 10)
    private String subject;
    private String imagename;
    @Column(columnDefinition = "TEXT NOT NULL")
    private String content;
    @Column(columnDefinition = "BIGINT NOT NULL DEFAULT 0")
    private Long view;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'BASIC'")   // enum(문자열)을 기본값으로 지정 시, 작은 따음표 추가 필요
    private CategoryCode category;

}
