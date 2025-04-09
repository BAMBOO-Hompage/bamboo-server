package SMU.BAMBOO.Hompage.domain.award.repository;

import SMU.BAMBOO.Hompage.domain.award.entity.Award;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.domain.subject.repository.dto.SubjectWeek;

import java.util.List;
import java.util.Optional;

public interface AwardRepository {
    Award getById(Long id);
    Optional<Award> findById(Long id);
    List<Award> findAll();
    List<Award> findByBatch(int batch);
    Award save(Award award);
    void delete(Long id);
    boolean existsByInventoryId(Long inventoryId);
    List<SubjectWeek> findLatestWeeksBySubjectInBatch(int batch);
    List<Award> findAllByBatchAndSubjectAndWeek(int batch, Subject subject, Integer week);
}
