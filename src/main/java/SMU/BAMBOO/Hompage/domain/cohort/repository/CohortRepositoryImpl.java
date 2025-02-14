package SMU.BAMBOO.Hompage.domain.cohort.repository;

import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
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
    public Cohort getByBatch(int batch) {
        return findByBatch(batch).orElseThrow(() -> new CustomException(ErrorCode.COHORT_NOT_EXIST));
    }

    @Override
    public Optional<Cohort> findById(Long id) {
        return cohortJpaRepository.findById(id);
    }

    @Override
    public Optional<Cohort> findByBatch(int batch) {
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
