package SMU.BAMBOO.Hompage.domain.inventory.repository;

import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    Optional<Inventory> findById(Long id);

    List<Inventory> findAll();

    Page<Inventory> findByPage(Pageable pageable);

    Inventory save(Inventory inventory);

    void deleteById(Long id);

    Boolean existsByMemberAndStudyAndWeek(Member member, Study study, int week);
}
