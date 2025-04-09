package SMU.BAMBOO.Hompage.domain.subject.repository;

import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository {
    Subject getById(Long id);
    Optional<Subject> findById(Long id);
    Optional<Subject> findByNameAndBatch(String name, int batch);
    List<Subject> findByIsBookAndBatch(Boolean isBook, int batch);
    List<Subject> findAll();
    Subject save(Subject subject);
    void deleteById(Long id);
    Subject getByBatchAndName(int batch, String subjectName);
}
