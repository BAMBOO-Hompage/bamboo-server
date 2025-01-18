package SMU.BAMBOO.Hompage.domain.mainActivites.repository;

import SMU.BAMBOO.Hompage.domain.mainActivites.entity.MainActivities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MainActivitiesRepository {
    MainActivities save(MainActivities mainActivities);
    Page<MainActivities> findByYear(int year, Pageable pageable);
}
