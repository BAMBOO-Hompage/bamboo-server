package SMU.BAMBOO.Hompage.domain.weeklyContent.repository;

import SMU.BAMBOO.Hompage.domain.weeklyContent.entity.WeeklyContent;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WeeklyContentRepositoryImpl implements WeeklyContentRepository {

    private final WeeklyContentJpaRepository weeklyContentJpaRepository;

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
    public List<WeeklyContent> findBySubject(Long subjectId) {
        return weeklyContentJpaRepository.findBySubjectId(subjectId);
    }

    @Override
    public Optional<WeeklyContent> findBySubjectIdAndWeek(Long subjectId, int week) {
        return weeklyContentJpaRepository.findBySubjectIdAndWeek(subjectId, week);
    }

    @Override
    public WeeklyContent save(WeeklyContent weeklyContent) {
        return weeklyContentJpaRepository.save(weeklyContent);
    }

    @Override
    public void delete(Long id) {
        weeklyContentJpaRepository.deleteById(id);
    }
}
