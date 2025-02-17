package SMU.BAMBOO.Hompage.domain.study.repository;

import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepository {

    private final StudyJpaRepository studyJpaRepository;

    @Override
    public Optional<Study> findById(Long id) {
        return studyJpaRepository.findById(id);
    }

    @Override
    public List<Study> findAll() {
        return studyJpaRepository.findAll();
    }

    @Override
    public List<Study> findByCohortAndSubject(Cohort cohort, Subject subject) {
        return studyJpaRepository.findByCohortAndSubject(cohort, subject);
    }

    @Override
    public Study save(Study study) {
        return studyJpaRepository.save(study);
    }

    @Override
    public void deleteById(Long id) {
        studyJpaRepository.deleteById(id);
    }
}
