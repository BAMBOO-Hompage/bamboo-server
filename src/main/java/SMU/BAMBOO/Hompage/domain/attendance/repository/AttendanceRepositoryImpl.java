package SMU.BAMBOO.Hompage.domain.attendance.repository;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.attendance.entity.QAttendance;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.QStudyWeek;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
        QAttendance attendance = QAttendance.attendance;
        QStudyWeek studyWeek = QStudyWeek.studyWeek;

        return queryFactory
                .selectFrom(attendance)
                .leftJoin(attendance.studyWeek, studyWeek).fetchJoin()
                .where(attendance.studyWeek.study.studyId.eq(studyId))
                .orderBy(attendance.studyWeek.week.asc())
                .fetch();
    }

    @Override
    public List<Attendance> findByStudyWeekIdIn(List<Long> studyWeekIds) {
        QAttendance attendance = QAttendance.attendance;
        QStudyWeek studyWeek = QStudyWeek.studyWeek;

        return queryFactory
                .selectFrom(attendance)
                .leftJoin(attendance.studyWeek, studyWeek).fetchJoin()
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
