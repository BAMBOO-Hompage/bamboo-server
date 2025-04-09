package SMU.BAMBOO.Hompage.domain.subject.repository;

import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static SMU.BAMBOO.Hompage.domain.cohort.entity.QCohort.cohort;
import static SMU.BAMBOO.Hompage.domain.subject.entity.QSubject.subject;

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
    public Optional<Subject> findByNameAndBatch(String name, int batch) {
        Subject foundSubject = queryFactory
                .selectFrom(subject)
                .leftJoin(subject.cohort, cohort).fetchJoin()
                .where(
                        subject.name.eq(name),
                        subject.cohort.batch.eq(batch)
                )
                .fetchOne();

        return Optional.ofNullable(foundSubject);
    }

    @Override
    public List<Subject> findAll() {
        return subjectJpaRepository.findAll();
    }

    @Override
    public List<Subject> findByIsBookAndBatch(Boolean isBook, int batch) {
        return queryFactory
                .selectFrom(subject)
                .leftJoin(subject.cohort, cohort).fetchJoin()
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

    @Override
    public Subject getByBatchAndName(int batch, String subjectName) {
        return Optional.ofNullable(
                        queryFactory
                                .selectFrom(subject)
                                .join(subject.cohort, cohort).fetchJoin()
                                .where(
                                        cohort.batch.eq(batch),
                                        subject.name.eq(subjectName)
                                )
                                .fetchOne()
                )
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_EXIST));
    }
}
