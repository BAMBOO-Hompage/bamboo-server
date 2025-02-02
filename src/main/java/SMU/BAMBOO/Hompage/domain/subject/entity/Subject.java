package SMU.BAMBOO.Hompage.domain.subject.entity;

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

    @Builder.Default
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<Study> studies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<WeeklyContent> weeklyContents = new ArrayList<>();

    public void updateName(SubjectRequestDTO.Update request) {
        this.name = request.name();
    }
}
