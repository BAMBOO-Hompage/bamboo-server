package SMU.BAMBOO.Hompage.domain.subject.repository;

import SMU.BAMBOO.Hompage.domain.study.dto.StudyResponseDTO;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository {
    Optional<Subject> findById(Long id);
    Optional<Subject> findByName(String name);
    List<Subject> findByIsBook(Boolean isBook);
    List<Subject> findAll();
    List<StudyResponseDTO.GetOne> findStudiesBySubjectId(Long subjectId);
    Subject save(Subject subject);
    void deleteById(Long id);
}
