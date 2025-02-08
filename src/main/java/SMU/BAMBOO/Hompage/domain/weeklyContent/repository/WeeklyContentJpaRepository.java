package SMU.BAMBOO.Hompage.domain.weeklyContent.repository;

import SMU.BAMBOO.Hompage.domain.weeklyContent.entity.WeeklyContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeeklyContentJpaRepository extends JpaRepository<WeeklyContent, Long> {

    List<WeeklyContent> findBySubjectId(Long subjectId);
    Optional<WeeklyContent> findBySubjectIdAndWeek(Long subjectId, int week);
}
