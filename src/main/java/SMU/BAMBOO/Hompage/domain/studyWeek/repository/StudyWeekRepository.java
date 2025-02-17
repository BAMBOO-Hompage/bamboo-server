package SMU.BAMBOO.Hompage.domain.studyWeek.repository;

import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;

import java.util.List;
import java.util.Optional;

public interface StudyWeekRepository {
    Optional<StudyWeek> findByStudyAndWeek(Study study, int week);
    Optional<StudyWeek> findById(Long id);
    List<StudyWeek> findByStudyOrderByWeekAsc(Study study);
    StudyWeek save(StudyWeek studyWeek);
}
