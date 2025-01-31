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
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = true)
    private Study study;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int week;

    @OneToOne(mappedBy = "inventory", fetch = FetchType.LAZY)
    private Award award;

    public void updateInventory(InventoryRequestDTO.Update updateRequest) {
        if (updateRequest.title() != null) {
            this.title = updateRequest.title();
        }
        if (updateRequest.content() != null) {
            this.content = updateRequest.content();
        }
        if (updateRequest.week() > 0) {
            this.week = updateRequest.week();
        }
    }

    public void addAward(Award award) {
        this.award = award;
    }
}
