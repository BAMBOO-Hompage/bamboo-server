package SMU.BAMBOO.Hompage.domain.subject.repository;

import SMU.BAMBOO.Hompage.domain.subject.entity.QSubject;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
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
    public Subject getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_EXIST));
    }

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
    public List<Subject> findByIsBookAndBatch(Boolean isBook, int batch) {
        QSubject subject = QSubject.subject;

        return queryFactory
                .selectFrom(subject)
                .where(
                        subject.cohort.batch.eq(batch),
                        isBook != null ? subject.isBook.eq(isBook) : null
                )
                .orderBy(subject.isBook.desc())
                .fetch();
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
