package SMU.BAMBOO.Hompage.domain.study.entity;

import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.entity.MemberStudy;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private int cohort;

    @Builder.Default
    @Column(name = "is_book", nullable = false)
    private Boolean isBook = false;

    @Column(nullable = false)
    private int section;

    @Column(name = "study_master", nullable = false)
    private String studyMaster;

    @Builder.Default
    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberStudy> memberStudies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    private List<Inventory> inventories = new ArrayList<>();

    public void updateStudy(Subject subject, int cohort, Boolean isBook, int section, String studyMaster, List<MemberStudy> updatedMemberStudies) {
        this.subject = subject;
        this.cohort = cohort;
        this.isBook = isBook;
        this.section = section;
        this.studyMaster = studyMaster;

        // NPE 방지를 위해 memberStudies 가 null 이라면 초기화
        if (this.memberStudies == null) {
            this.memberStudies = new ArrayList<>();
        }

        // 기존 MemberStudy 리스트를 순회하면서 관계 제거
        this.memberStudies.forEach(memberStudy -> memberStudy.associateStudy(null));
        this.memberStudies.clear();

        // 새로운 MemberStudy 추가
        this.memberStudies.addAll(updatedMemberStudies);
    }


    /**
     * 연관 관계 편의 메서드
     */
    public void addMemberStudy(MemberStudy memberStudy) {
        this.memberStudies.add(memberStudy);
        memberStudy.associateStudy(this);
    }
}
