package SMU.BAMBOO.Hompage.domain.award.repository;

import SMU.BAMBOO.Hompage.domain.award.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwardJpaRepository extends JpaRepository<Award, Long> {
    List<Award> findByBatch(int batch);
}
