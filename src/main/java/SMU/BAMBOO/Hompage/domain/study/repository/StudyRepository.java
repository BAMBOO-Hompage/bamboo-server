package SMU.BAMBOO.Hompage.domain.study.repository;

import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface StudyRepository {
    Optional<Study> findById(Long id);
    List<Study> findAll();
    List<Study> findByCohortAndSubject(Cohort cohort, Subject subject);
    Study save(Study study);
    void deleteById(Long id);
}
