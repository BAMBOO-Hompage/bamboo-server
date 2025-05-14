package SMU.BAMBOO.Hompage.domain.inventory.entity;

import SMU.BAMBOO.Hompage.domain.award.entity.Award;
import SMU.BAMBOO.Hompage.domain.inventory.dto.InventoryRequestDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Inventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int week;

    @Column(name = "is_weekly_best")
    private Boolean isWeeklyBest;

    @Column(name = "file_url")
    private String fileUrl;

    @OneToOne(mappedBy = "inventory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Award award;

    public void updateInventory(InventoryRequestDTO.Update updateRequest, String fileUrl) {
        if (updateRequest.title() != null) {
            this.title = updateRequest.title();
        }
        if (updateRequest.content() != null) {
            this.content = updateRequest.content();
        }
        if (updateRequest.week() > 0) {
            this.week = updateRequest.week();
        }
        this.fileUrl = fileUrl;
    }

    public void markAsWeeklyBest() {
        this.isWeeklyBest = true;
    }

    public void removeFile() {
        this.fileUrl = null;
    }
}
