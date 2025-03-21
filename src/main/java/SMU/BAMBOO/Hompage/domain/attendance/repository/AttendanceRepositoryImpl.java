package SMU.BAMBOO.Hompage.domain.attendance.repository;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static SMU.BAMBOO.Hompage.domain.attendance.entity.QAttendance.attendance;
import static SMU.BAMBOO.Hompage.domain.member.entity.QMember.member;
import static SMU.BAMBOO.Hompage.domain.studyWeek.entity.QStudyWeek.studyWeek;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {

    private final AttendanceJpaRepository attendanceJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Attendance> findByStudyWeekId(Long studyWeekId) {
        return attendanceJpaRepository.findByStudyWeekId(studyWeekId);
    }

    @Override
    public List<Attendance> findByStudyId(Long studyId) {
        return queryFactory
                .selectFrom(attendance)
                .leftJoin(attendance.studyWeek, studyWeek).fetchJoin()
                .leftJoin(attendance.member, member).fetchJoin()
                .where(attendance.studyWeek.study.studyId.eq(studyId))
                .orderBy(attendance.studyWeek.week.asc())
                .fetch();
    }

    @Override
    public List<Attendance> findByStudyWeekIdIn(List<Long> studyWeekIds) {
        return queryFactory
                .selectFrom(attendance)
                .leftJoin(attendance.studyWeek, studyWeek).fetchJoin()
                .leftJoin(attendance.member, member).fetchJoin()
                .where(attendance.studyWeek.id.in(studyWeekIds))
                .orderBy(attendance.studyWeek.week.asc())
                .fetch();
    }

    @Override
    public void saveAll(List<Attendance> attendances) {
        attendanceJpaRepository.saveAll(attendances);
    }

    @Override
    public Optional<Attendance> findByStudyWeekAndMember(StudyWeek studyWeek, Member member) {
        return attendanceJpaRepository.findByStudyWeekAndMember(studyWeek, member);
    }
}
