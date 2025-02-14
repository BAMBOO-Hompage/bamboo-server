package SMU.BAMBOO.Hompage.domain.cohort.repository;

import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CohortJpaRepository extends JpaRepository<Cohort, Long> {
    Optional<Cohort> findByBatch(int batch);
}
