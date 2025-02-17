package SMU.BAMBOO.Hompage.domain.studyWeek.repository;

import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyWeekRepositoryImpl implements StudyWeekRepository {

    private final StudyWeekJpaRepository studyWeekJpaRepository;

    @Override
    public Optional<StudyWeek> findByStudyAndWeekNumber(Study study, int week) {
        return studyWeekJpaRepository.findByStudyAndWeekNumber(study, week);
    }

    @Override
    public Optional<StudyWeek> findById(Long id) {
        return studyWeekJpaRepository.findById(id);
    }

    @Override
    public StudyWeek save(StudyWeek studyWeek) {
        return studyWeekJpaRepository.save(studyWeek);
    }
}
