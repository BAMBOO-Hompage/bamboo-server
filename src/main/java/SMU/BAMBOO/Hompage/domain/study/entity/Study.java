package SMU.BAMBOO.Hompage.domain.study.entity;

import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;
import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.entity.MemberStudy;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Study extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long studyId;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cohort_id", nullable = false)
    private Cohort cohort;

    @Builder.Default
    @Column(name = "is_book", nullable = false)
    private Boolean isBook = false;

    @Column(nullable = false)
    private int section;

    @Column(name = "study_master_id")
    private Long studyMasterId;

    @Column(name = "study_master_name")
    private String studyMasterName;

    @Column(name = "study_master_student_id")
    private String studyMasterStudentId;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "study_id", insertable = false, updatable = false)
    private List<MemberStudy> memberStudies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventory> inventories = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyWeek> studyWeeks = new ArrayList<>();

    public void updateStudy(
            Subject subject,
            String teamName,
            Cohort cohort,
            Boolean isBook,
            int section,
            Member studyMaster,
            List<MemberStudy> updatedMemberStudies
    ) {
        this.subject = subject;
        this.teamName = teamName;
        this.cohort = cohort;
        this.isBook = isBook;
        this.section = section;

        // 스터디장 정보 설정
        this.studyMasterId = studyMaster.getMemberId();
        this.studyMasterName = studyMaster.getName();
        this.studyMasterStudentId = studyMaster.getStudentId();

        // 기존 MemberStudy 삭제 후 교체
        this.memberStudies.clear();
        this.memberStudies.addAll(updatedMemberStudies);
    }

}
