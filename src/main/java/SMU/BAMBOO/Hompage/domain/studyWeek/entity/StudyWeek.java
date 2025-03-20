package SMU.BAMBOO.Hompage.domain.studyWeek.entity;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_week")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StudyWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_week_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Column(nullable = false)
    private int week;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "studyWeek", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Attendance> attendances = new ArrayList<>();

    public static StudyWeek create(Study study, int week) {
        return StudyWeek.builder()
                .study(study)
                .week(week)
                .build();
    }

    /** 이미지 update 메서드 */
    public void updateWeekImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
