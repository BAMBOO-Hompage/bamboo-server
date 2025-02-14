package SMU.BAMBOO.Hompage.domain.cohort.entity;

import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cohort")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Cohort extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cohort_id")
    private Long cohortId;

    @Column(nullable = false)
    private int batch; // 기수

    @Column(nullable = false)
    private int year;

    @Column(name = "is_first_semester", nullable = false)
    private boolean isFirstSemester;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @OneToMany(mappedBy = "cohort", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Subject> subjects = new ArrayList<>();

    /**
     * 활동 상태 변경 메서드 (활동 종료)
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * 과목 추가 연관 관계 편의 메서드
     */
    public void addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.associateCohort(this);
    }
}
