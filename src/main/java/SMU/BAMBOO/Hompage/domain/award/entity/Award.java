package SMU.BAMBOO.Hompage.domain.award.entity;

import SMU.BAMBOO.Hompage.domain.award.dto.AwardRequestDTO;
import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "award")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Award extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "award_id")
    private Long awardId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false, unique = true)
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private int batch;

    // true = MIDTERM, false = FINAL
    @Column(name = "is_midterm", nullable = false)
    private boolean isMidterm;

    public void updateAward(Inventory inventory, AwardRequestDTO.Update request) {
        this.inventory = inventory;
        this.batch = request.batch();
        this.isMidterm = request.isMidterm();
    }

    /** 연관 관계 삭제를 위한 메서드 */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
