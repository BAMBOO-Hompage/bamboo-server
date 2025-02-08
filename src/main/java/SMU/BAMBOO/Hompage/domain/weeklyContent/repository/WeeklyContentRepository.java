package SMU.BAMBOO.Hompage.domain.weeklyContent.repository;

import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.domain.weeklyContent.entity.WeeklyContent;

import java.util.List;
import java.util.Optional;

public interface WeeklyContentRepository {
    Optional<WeeklyContent> findById(Long id);
    WeeklyContent getById(Long id);
    List<WeeklyContent> findBySubject(Subject subject);
    Optional<WeeklyContent> findBySubjectAndWeek(Subject subject, int week);
    WeeklyContent save(WeeklyContent weeklyContent);
    void delete(Long id);
}
