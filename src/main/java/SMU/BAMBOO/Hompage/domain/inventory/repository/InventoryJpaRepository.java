package SMU.BAMBOO.Hompage.domain.inventory.repository;

import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryJpaRepository extends JpaRepository<Inventory, Long> {

    boolean existsByMemberAndStudyAndWeek(Member member, Study study, int week);
}
