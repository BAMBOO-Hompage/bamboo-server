package SMU.BAMBOO.Hompage.domain.mainActivites.entity;

import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "main_activities")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MainActivities extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "main_activities_id")
    private Long mainActivitiesId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
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

    public static MainActivities from(MainActivitiesRequestDTO.Create request, List<String> images) {
        if (images == null) {
            images = new ArrayList<>();
        }
        return MainActivities.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .year(request.getYear())
                .images(images)
                .build();
    }

    public void update(MainActivitiesRequestDTO.Update request, List<String> newImages) {
        this.title = request.getTitle();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.year = request.getYear();
        this.images = new ArrayList<>(newImages);
    }

}
