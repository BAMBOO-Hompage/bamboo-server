package SMU.BAMBOO.Hompage.domain.study.repository;

import SMU.BAMBOO.Hompage.domain.cohort.entity.QCohort;
import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.entity.QMemberStudy;
import SMU.BAMBOO.Hompage.domain.member.entity.QMember;
import SMU.BAMBOO.Hompage.domain.study.entity.QStudy;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.subject.entity.QSubject;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepository {

    private final StudyJpaRepository studyJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Study> findById(Long id) {
        QStudy study = QStudy.study;
        QMemberStudy memberStudy = QMemberStudy.memberStudy;
        QMember member = QMember.member;
        QSubject subject = QSubject.subject;
        QCohort cohort = QCohort.cohort;

        Study result = queryFactory
                .selectFrom(study)
                .leftJoin(study.subject, subject).fetchJoin()
                .leftJoin(study.cohort, cohort).fetchJoin()
                .leftJoin(study.memberStudies, memberStudy).fetchJoin()
                .leftJoin(memberStudy.member, member).fetchJoin()
                .where(study.studyId.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Study> findAll() {
        QStudy study = QStudy.study;
        QMemberStudy memberStudy = QMemberStudy.memberStudy;
        QMember member = QMember.member;
        QSubject subject = QSubject.subject;
        QCohort cohort = QCohort.cohort;

        return queryFactory
                .selectFrom(study)
                .leftJoin(study.subject, subject).fetchJoin()
                .leftJoin(study.cohort, cohort).fetchJoin()
                .leftJoin(study.memberStudies, memberStudy).fetchJoin()
                .leftJoin(memberStudy.member, member).fetchJoin()
                .orderBy(study.section.asc())
                .fetch();
    }


    @Override
    public List<Study> findByCohortAndSubject(Long cohortId, Long subjectId) {
        QStudy study = QStudy.study;
        QMemberStudy memberStudy = QMemberStudy.memberStudy;
        QMember member = QMember.member;
        QSubject subjectEntity = QSubject.subject;
        QCohort cohortEntity = QCohort.cohort;

        return queryFactory
                .selectFrom(study)
                .leftJoin(study.subject, subjectEntity).fetchJoin()
                .leftJoin(study.cohort, cohortEntity).fetchJoin()
                .leftJoin(study.memberStudies, memberStudy).fetchJoin()
                .leftJoin(memberStudy.member, member).fetchJoin()
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
