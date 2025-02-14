package SMU.BAMBOO.Hompage.domain.subject.repository;

import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectJpaRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByName(String name);
    List<Subject> findByIsBook(Boolean isBook);
    List<Subject> findByIsBookAndCohort_Batch(Boolean isBook, int batch);
    List<Subject> findByCohort_Batch(int batch);
}
