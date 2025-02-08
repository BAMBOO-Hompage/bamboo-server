package SMU.BAMBOO.Hompage.domain.weeklyContent.entity;

import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.domain.weeklyContent.dto.WeeklyContentRequestDTO;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weekly_content")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class WeeklyContent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekly_content_id")
    private Long weeklyContentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int week;

    public void update(WeeklyContentRequestDTO.Update request) {
        this.content = request.content();
        this.week = request.week();
    }
}
