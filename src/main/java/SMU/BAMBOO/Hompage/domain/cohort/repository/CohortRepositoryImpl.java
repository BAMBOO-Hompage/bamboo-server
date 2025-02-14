package SMU.BAMBOO.Hompage.domain.cohort.repository;

import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CohortRepositoryImpl implements CohortRepository {

    private final CohortJpaRepository cohortJpaRepository;

    @Override
    public Cohort save(Cohort cohort) {
        return cohortJpaRepository.save(cohort);
    }

    @Override
    public Optional<Cohort> findById(Long id) {
        return cohortJpaRepository.findById(id);
    }

    @Override
    public Optional<Cohort> findByYearAndIsFirstSemester(int year, boolean isFirstSemester) {
        return cohortJpaRepository.findByYearAndIsFirstSemester(year, isFirstSemester);
    }

    @Override
    public boolean existsByYearAndSemester(int year, boolean isFirstSemester) {
        return cohortJpaRepository.existsByYearAndIsFirstSemester(year, isFirstSemester);
    }

    @Override
    public List<Cohort> findByBatch(int batch) {
        return cohortJpaRepository.findByBatch(batch);
    }

    @Override
    public List<Cohort> findAll() {
        return cohortJpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        cohortJpaRepository.deleteById(id);
    }
}
