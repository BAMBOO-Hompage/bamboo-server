package SMU.BAMBOO.Hompage.domain.cohort.entity;

import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

}
