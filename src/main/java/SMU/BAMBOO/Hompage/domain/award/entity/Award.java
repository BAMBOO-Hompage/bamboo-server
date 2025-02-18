package SMU.BAMBOO.Hompage.domain.award.entity;

import SMU.BAMBOO.Hompage.domain.award.dto.AwardRequestDTO;
import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "award")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Award extends BaseEntity {

    // FIXME 컬럼 정리 필요 (추후에 요구사항 확실히 정해지면)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "award_id")
    private Long awardId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false, unique = true)
    private Inventory inventory;

    @Column(nullable = false)
    private int batch;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false)
    private int week;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    public void updateAward(Inventory inventory, AwardRequestDTO.Update request) {
        this.inventory = inventory;
        this.batch = request.batch();
        this.title = request.title();
        this.week = request.week();
        this.startDate = request.startDate();
        this.endDate = request.endDate();
    }
}
