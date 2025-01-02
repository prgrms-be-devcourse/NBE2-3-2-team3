package com.example.bestme.domain.community;

import com.example.bestme.domain.BaseEntity;
import com.example.bestme.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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

    // FetchType.LAZY: 연관된 데이터를 지연 로딩합니다. (UserEntity는 필요할 때만 로드)
    // referencedColumnName : 참조 대상 엔티티의 PK가 아닌 다른 컬럼을 외래 키로 참조해야 할 때 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "user_id", nullable = false)    // FK 매핑
    @OnDelete(action = OnDeleteAction.CASCADE)      // 단방향 때에도, 해당 유저가 삭제되면 게시물이 자동 삭제되게 설정
    private User user;

    @Column( length = 20, nullable = false )
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
