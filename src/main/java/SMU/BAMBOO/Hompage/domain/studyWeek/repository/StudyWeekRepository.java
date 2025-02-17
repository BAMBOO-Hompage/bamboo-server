package SMU.BAMBOO.Hompage.domain.studyWeek.repository;

import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;

import java.util.Optional;

public interface StudyWeekRepository {
    Optional<StudyWeek> findByStudyAndWeekNumber(Study study, int week);
    Optional<StudyWeek> findById(Long id);
    StudyWeek save(StudyWeek studyWeek);
}
