package SMU.BAMBOO.Hompage.domain.mainActivites.repository;

import SMU.BAMBOO.Hompage.domain.mainActivites.entity.MainActivities;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MainActivitiesRepositoryImpl implements MainActivitiesRepository {

    private final MainActivitiesJpaRepository mainActivitiesJpaRepository;

    @Override
    public MainActivities save(MainActivities mainActivities){
        return mainActivitiesJpaRepository.save(mainActivities);
    }

    @Override
    public Page<MainActivities> findByYear(int year, Pageable pageable) {
        return mainActivitiesJpaRepository.findByYear(year, pageable);
    }

    @Override
    public Optional<MainActivities> findById(Long id){
        return mainActivitiesJpaRepository.findById(id);
    }

    @Override
    public void deleteById(Long id){
        mainActivitiesJpaRepository.deleteById(id);
    }
}
