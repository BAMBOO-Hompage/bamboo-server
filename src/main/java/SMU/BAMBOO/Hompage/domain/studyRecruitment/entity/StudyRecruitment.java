package SMU.BAMBOO.Hompage.domain.studyRecruitment.entity;

import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.studyRecruitment.dto.StudyRecruitmentRequestDTO;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "study_recruitment")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StudyRecruitment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_recruitment_id")
    private Long id;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "writer_name", nullable = false)
    private String writerName;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int maxMembers;

    @Column(nullable = false)
    private LocalDate deadline;

    public static StudyRecruitment from(StudyRecruitmentRequestDTO.Create request, Member member) {
        return StudyRecruitment.builder()
                .writerId(member.getMemberId())
                .writerName(member.getName())
                .title(request.title())
                .content(request.content())
                .maxMembers(request.maxMembers())
                .deadline(request.deadline())
                .build();
    }

    public void update(StudyRecruitmentRequestDTO.Update request) {
        this.title = request.title();
        this.content = request.content();
        this.maxMembers = request.maxMembers();
        this.deadline = request.deadline();
    }
}
