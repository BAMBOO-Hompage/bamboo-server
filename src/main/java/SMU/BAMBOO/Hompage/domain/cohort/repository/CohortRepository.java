package SMU.BAMBOO.Hompage.domain.cohort.repository;

import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;

import java.util.List;
import java.util.Optional;

public interface CohortRepository {
    Cohort save(Cohort cohort);
    Optional<Cohort> findById(Long id);
    Cohort getByBatch(int batch);
    Optional<Cohort> findByBatch(int batch);
    List<Cohort> findAll();
    void deleteById(Long id);
}

