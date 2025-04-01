package SMU.BAMBOO.Hompage.domain.award.repository;

import SMU.BAMBOO.Hompage.domain.award.entity.Award;

import java.util.List;
import java.util.Optional;

public interface AwardRepository {
    Award getById(Long id);
    Optional<Award> findById(Long id);
    List<Award> findAll();
    List<Award> findByBatch(int batch);
    Award save(Award award);
    void delete(Long id);
    Integer findLatestWeekByBatch(int batch);
    List<Award> findAllByBatchAndWeek(int batch, int week);
    boolean existsByInventoryId(Long inventoryId);
}
