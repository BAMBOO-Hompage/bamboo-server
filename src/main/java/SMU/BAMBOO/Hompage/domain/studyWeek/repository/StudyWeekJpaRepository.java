package SMU.BAMBOO.Hompage.domain.studyWeek.repository;

import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyWeekJpaRepository extends JpaRepository<StudyWeek, Long> {
    Optional<StudyWeek> findByStudyAndWeekNumber(Study study, int week);
}
