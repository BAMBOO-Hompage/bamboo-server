package SMU.BAMBOO.Hompage.domain.cohort.repository;

import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;

import java.util.List;
import java.util.Optional;

public interface CohortRepository {
    Cohort save(Cohort cohort);
    Optional<Cohort> findById(Long id);
    Optional<Cohort> findByYearAndIsFirstSemester(int year, boolean isFirstSemester);
    boolean existsByYearAndSemester(int year, boolean isFirstSemester);
    List<Cohort> findByBatch(int batch);
    List<Cohort> findAll();
    void deleteById(Long id);
}

