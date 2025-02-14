package SMU.BAMBOO.Hompage.domain.subject.repository;

import SMU.BAMBOO.Hompage.domain.study.dto.StudyResponseDTO;
import SMU.BAMBOO.Hompage.domain.study.entity.QStudy;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubjectRepositoryImpl implements SubjectRepository {

    private final SubjectJpaRepository subjectJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Subject> findById(Long id) {
        return subjectJpaRepository.findById(id);
    }

    @Override
    public Optional<Subject> findByName(String name) {
        return subjectJpaRepository.findByName(name);
    }

    @Override
    public List<Subject> findAll() {
        return subjectJpaRepository.findAll();
    }

    @Override
    public List<StudyResponseDTO.GetOne> findStudiesBySubjectId(Long subjectId) {
        QStudy study = QStudy.study;

        return queryFactory
                .selectFrom(study)
                .where(study.subject.subjectId.eq(subjectId))
                .fetch()
                .stream()
                .map(StudyResponseDTO.GetOne::from)
                .toList();
    }

    @Override
    public List<Subject> findByIsBook(Boolean isBook) {
        return subjectJpaRepository.findByIsBook(isBook);
    }

    @Override
    public List<Subject> findByIsBookAndCohort_Batch(Boolean isBook, int batch) {
        return subjectJpaRepository.findByIsBookAndCohort_Batch(isBook, batch);
    }

    @Override
    public List<Subject> findByCohort_Batch(int batch) {
        return subjectJpaRepository.findByCohort_Batch(batch);
    }

    @Override
    public Subject save(Subject subject) {
        return subjectJpaRepository.save(subject);
    }

    @Override
    public void deleteById(Long id) {
        subjectJpaRepository.deleteById(id);
    }
}
