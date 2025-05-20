package SMU.BAMBOO.Hompage.domain.mainActivites.repository;

import SMU.BAMBOO.Hompage.domain.mainActivites.entity.MainActivities;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static SMU.BAMBOO.Hompage.domain.mainActivites.entity.QMainActivities.mainActivities;

@Repository
@RequiredArgsConstructor
public class MainActivitiesRepositoryImpl implements MainActivitiesRepository {

    private final MainActivitiesJpaRepository mainActivitiesJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public MainActivities save(MainActivities mainActivities){
        return mainActivitiesJpaRepository.save(mainActivities);
    }

    @Override
    public Page<MainActivities> findByYear(int year, Pageable pageable) {
        List<MainActivities> results = queryFactory
                .selectFrom(mainActivities)
                .where(mainActivities.year.eq(year))
                .orderBy(mainActivities.startDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total =  Optional.ofNullable(queryFactory
                .select(mainActivities.count())
                .from(mainActivities)
                .where(mainActivities.year.eq(year))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(results, pageable, total);
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
