package SMU.BAMBOO.Hompage.domain.subject.entity;

import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.subject.dto.SubjectRequestDTO;
import SMU.BAMBOO.Hompage.domain.weeklyContent.entity.WeeklyContent;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subject")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Subject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(name = "book_name", length = 50)
    private String bookName;

    @Column(name = "is_book", nullable = false)
    private Boolean isBook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cohort_id", nullable = false)
    private Cohort cohort;

    @Builder.Default
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<Study> studies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<WeeklyContent> weeklyContents = new ArrayList<>();

    public void update(SubjectRequestDTO.Update request, Cohort cohort) {
        this.name = request.name();
        this.bookName = request.bookName();
        this.isBook = request.isBook();
        this.cohort = cohort;
    }

    /** 연관관계 편의 메서드 */
    public void addWeeklyContent(WeeklyContent weeklyContent) {
        this.weeklyContents.add(weeklyContent);
        weeklyContent.associateSubject(this);
    }

    /** 기수 설정 메서드 */
    public void associateCohort(Cohort cohort) {
        this.cohort = cohort;
    }
}
