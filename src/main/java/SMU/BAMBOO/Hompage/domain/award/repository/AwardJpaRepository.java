package SMU.BAMBOO.Hompage.domain.award.repository;

import SMU.BAMBOO.Hompage.domain.award.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardJpaRepository extends JpaRepository<Award, Long> {
}
