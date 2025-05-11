package SMU.BAMBOO.Hompage.domain.weeklyContent.repository;

import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.domain.weeklyContent.entity.WeeklyContent;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static SMU.BAMBOO.Hompage.domain.weeklyContent.entity.QWeeklyContent.weeklyContent;

@Repository
@RequiredArgsConstructor
public class WeeklyContentRepositoryImpl implements WeeklyContentRepository {

    private final WeeklyContentJpaRepository weeklyContentJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<WeeklyContent> findById(Long id) {
        return weeklyContentJpaRepository.findById(id);
    }

    @Override
    public WeeklyContent getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.WEEKLY_CONTENT_NOT_EXIST));
    }

    @Override
    public List<WeeklyContent> findBySubject(Subject subject) {
        return weeklyContentJpaRepository.findBySubject(subject);
    }

    @Override
    public Optional<WeeklyContent> findBySubjectAndWeek(Subject subject, int week) {
        return weeklyContentJpaRepository.findBySubjectAndWeek(subject, week);
    }

    @Override
    public WeeklyContent save(WeeklyContent weeklyContent) {
        return weeklyContentJpaRepository.save(weeklyContent);
    }

    @Override
    public void delete(Long id) {
        weeklyContentJpaRepository.deleteById(id);
    }

    @Override
    public void deleteBySubjcetId(Long subjectId) {
        queryFactory
                .delete(weeklyContent)
                .where(weeklyContent.subject.subjectId.eq(subjectId))
                .execute();
    }
}
