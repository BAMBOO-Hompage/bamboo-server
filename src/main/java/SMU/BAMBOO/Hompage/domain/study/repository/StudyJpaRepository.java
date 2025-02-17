package SMU.BAMBOO.Hompage.domain.study.repository;

import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyJpaRepository extends JpaRepository<Study, Long> {

    List<Study> findByCohortAndSubject(Cohort cohort, Subject subject);
}
