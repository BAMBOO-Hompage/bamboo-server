package SMU.BAMBOO.Hompage.domain.mapping.memberStudy.entity;

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
    private Long memberStudyId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "member_student_id", nullable = false)
    private String memberStudentId;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "study_id", nullable = false)
    private Long studyId;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setStudyId(Long studyId) {
        this.studyId = studyId;
    }
}

