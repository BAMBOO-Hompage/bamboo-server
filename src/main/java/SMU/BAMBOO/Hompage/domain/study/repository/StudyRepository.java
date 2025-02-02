package SMU.BAMBOO.Hompage.domain.study.repository;

import SMU.BAMBOO.Hompage.domain.study.entity.Study;

import java.util.List;
import java.util.Optional;

public interface StudyRepository {

    Optional<Study> findById(Long id);

    List<Study> findAll();

    Study save(Study study);

    void deleteById(Long id);
}
