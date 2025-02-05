package SMU.BAMBOO.Hompage.domain.award.entity;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "award_id")
    private Long awardId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false, unique = true)
    private Inventory inventory;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false, length = 100)
    private String chapter;

    @Column(nullable = false)
    private int session;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false)
    private int week;

    @Column(nullable = false)
    private LocalDate date;

}
