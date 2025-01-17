package SMU.BAMBOO.Hompage.domain.mainActivites.entity;

import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "main_activities")
@Builder(toBuilder = true)
// 위 항목은 하드코딩을 위한 항목이니 삭제!
// @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MainActivities extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "main_activities_id")
    private Long mainActivitiesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDate endDate;

    @Column(nullable = false)
    private int year;

    @Builder.Default
    @Column(nullable = false)
    private int views = 0;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "main_activities_images", joinColumns = @JoinColumn(name = "main_activities_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    public static MainActivities from(MainActivitiesRequestDTO.Create request, Member member, List<String> images) {
        return MainActivities.builder()
                .member(member)
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .year(request.getYear())
                .images(images)
                .build();
    }


}
