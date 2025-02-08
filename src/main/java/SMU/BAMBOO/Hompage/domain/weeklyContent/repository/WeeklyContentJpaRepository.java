package SMU.BAMBOO.Hompage.domain.weeklyContent.repository;

import SMU.BAMBOO.Hompage.domain.weeklyContent.entity.WeeklyContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyContentJpaRepository extends JpaRepository<WeeklyContent, Long> {

    List<WeeklyContent> findBySubjectId(Long subjectId);
}
