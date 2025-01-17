package SMU.BAMBOO.Hompage.domain.mainActivites.repository;

import SMU.BAMBOO.Hompage.domain.mainActivites.entity.MainActivities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MainActivitiesRepositoryImpl implements MainActivitiesRepository {

    private final MainActivitiesJpaRepository mainActivitiesJpaRepository;

    @Override
    public MainActivities save(MainActivities mainActivities){
        return mainActivitiesJpaRepository.save(mainActivities);
    }
}
