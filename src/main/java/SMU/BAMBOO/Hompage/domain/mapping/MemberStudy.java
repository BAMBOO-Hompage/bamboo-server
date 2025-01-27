package SMU.BAMBOO.Hompage.domain.mapping;

import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_study")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberStudy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_study_id")
    private Long member_study_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Builder.Default
    @Column(name = "o_count", nullable = false)
    private int oCount = 0;

    @Builder.Default
    @Column(name = "x_count", nullable = false)
    private int xCount = 0;

    /**
     * Study 연관 관계 편의 메서드
     */
    public void associateStudy(Study study) {
        this.study = study;
        if (!study.getMemberStudies().contains(this)) {
            study.getMemberStudies().add(this);
        }
    }

    /**
     * Member 연관 관계 편의 메서드
     */
    public void associateMember(Member member) {
        this.member = member;
        if (!member.getMemberStudies().contains(this)) {
            member.getMemberStudies().add(this);
        }
    }
}
