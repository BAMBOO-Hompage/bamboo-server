package SMU.BAMBOO.Hompage.domain.study.repository;

import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static SMU.BAMBOO.Hompage.domain.cohort.entity.QCohort.cohort;
import static SMU.BAMBOO.Hompage.domain.mapping.memberStudy.entity.QMemberStudy.memberStudy;
import static SMU.BAMBOO.Hompage.domain.study.entity.QStudy.study;
import static SMU.BAMBOO.Hompage.domain.subject.entity.QSubject.subject;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepository {

    private final StudyJpaRepository studyJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Study> findById(Long id) {
        Study result = queryFactory
                .selectFrom(study)
                .leftJoin(study.subject, subject).fetchJoin()
                .leftJoin(study.cohort, cohort).fetchJoin()
                .leftJoin(study.memberStudies, memberStudy).fetchJoin()
                .where(study.studyId.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Study> findAll() {
        return queryFactory
                .selectFrom(study)
                .leftJoin(study.subject, subject).fetchJoin()
                .leftJoin(study.cohort, cohort).fetchJoin()
                .leftJoin(study.memberStudies, memberStudy).fetchJoin()
                .orderBy(study.section.asc())
                .fetch();
    }


    @Override
    public List<Study> findByCohortAndSubject(Long cohortId, Long subjectId) {
        return queryFactory
                .selectFrom(study)
                .leftJoin(study.subject, subject).fetchJoin()
                .leftJoin(study.cohort, cohort).fetchJoin()
                .leftJoin(study.memberStudies, memberStudy).fetchJoin()
                .where(
                        study.cohort.cohortId.eq(cohortId),
                        study.subject.subjectId.eq(subjectId)
                )
                .orderBy(study.section.asc())
                .fetch();
    }

    @Override
    public Study save(Study study) {
        return studyJpaRepository.save(study);
    }

    @Override
    public void deleteById(Long id) {
        studyJpaRepository.deleteById(id);
    }
}
