package SMU.BAMBOO.Hompage.domain.award.repository;

import SMU.BAMBOO.Hompage.domain.award.entity.Award;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.domain.subject.repository.dto.SubjectWeek;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static SMU.BAMBOO.Hompage.domain.award.entity.QAward.award;

@Repository
@RequiredArgsConstructor
public class AwardRepositoryImpl implements AwardRepository {

    private final AwardJpaRepository awardJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Award getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.AWARD_NOT_EXIST));
    }

    @Override
    public Optional<Award> findById(Long id) {
        return awardJpaRepository.findById(id);
    }

    @Override
    public List<Award> findAll() {
        return awardJpaRepository.findAll();
    }

    @Override
    public List<Award> findByBatch(int batch) {
        return awardJpaRepository.findByBatch(batch);
    }

    @Override
    public Award save(Award award) {
        return awardJpaRepository.save(award);
    }

    @Override
    public void delete(Long id) {
        awardJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByInventoryId(Long inventoryId) {
        return Optional.ofNullable(
                queryFactory
                        .selectOne()
                        .from(award)
                        .where(award.inventory.inventoryId.eq(inventoryId))
                        .fetchFirst()
        ).isPresent();
    }

    @Override
    public List<SubjectWeek> findLatestWeeksBySubjectInBatch(int batch) {
        return queryFactory
                .select(Projections.constructor(SubjectWeek.class,
                        award.subject,
                        award.week.max()))
                .from(award)
                .where(award.batch.eq(batch))
                .groupBy(award.subject)
                .fetch();
    }

    @Override
    public List<Award> findAllByBatchAndSubjectAndWeek(int batch, Subject subject, Integer week) {
        return queryFactory
                .selectFrom(award)
                .where(
                        award.batch.eq(batch),
                        award.subject.eq(subject),
                        award.week.eq(week)
                )
                .fetch();
    }
}
