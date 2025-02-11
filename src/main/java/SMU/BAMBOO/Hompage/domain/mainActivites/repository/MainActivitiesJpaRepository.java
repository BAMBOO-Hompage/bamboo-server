package SMU.BAMBOO.Hompage.domain.mainActivites.repository;

import SMU.BAMBOO.Hompage.domain.mainActivites.entity.MainActivities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainActivitiesJpaRepository extends JpaRepository<MainActivities, Long> {
    Page<MainActivities> findByYear(int year, Pageable pageable);
}
