package SMU.BAMBOO.Hompage.domain.mainActivites.entity;

import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int views = 0;

    @ElementCollection
    @CollectionTable(name = "main_activities_images", joinColumns = @JoinColumn(name = "main_activities_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();


}
