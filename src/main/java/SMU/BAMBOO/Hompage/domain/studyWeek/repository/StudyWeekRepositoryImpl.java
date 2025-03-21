package SMU.BAMBOO.Hompage.domain.studyWeek.repository;

import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static SMU.BAMBOO.Hompage.domain.studyWeek.entity.QStudyWeek.studyWeek;

@Repository
@RequiredArgsConstructor
public class StudyWeekRepositoryImpl implements StudyWeekRepository {

    private final StudyWeekJpaRepository studyWeekJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<StudyWeek> findByStudyAndWeek(Study study, int week) {
        StudyWeek result = queryFactory
                .selectFrom(studyWeek)
                .where(
                        studyWeek.study.studyId.eq(study.getStudyId()),
                        studyWeek.week.eq(week)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<StudyWeek> findById(Long id) {
        return studyWeekJpaRepository.findById(id);
    }

    @Override
    public List<StudyWeek> findByStudyOrderByWeekAsc(Study study) {
        return studyWeekJpaRepository.findByStudyOrderByWeekAsc(study);
    }

    @Override
    public StudyWeek save(StudyWeek studyWeek) {
        return studyWeekJpaRepository.save(studyWeek);
    }

}
