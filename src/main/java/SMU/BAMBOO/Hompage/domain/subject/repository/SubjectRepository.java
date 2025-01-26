package SMU.BAMBOO.Hompage.domain.subject.repository;

import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository {

    Optional<Subject> findById(Long id);

    Optional<Subject> findByName(String name);

    List<Subject> findAll();

    Subject save(Subject subject);

    void deleteById(Long id);
}
