package SMU.BAMBOO.Hompage.domain.attendance.repository;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {

    private final AttendanceJpaRepository attendanceJpaRepository;

    @Override
    public List<Attendance> findByStudyWeekId(Long studyWeekId) {
        return attendanceJpaRepository.findByStudyWeekId(studyWeekId);
    }

    @Override
    public List<Attendance> findByStudyId(Long studyId) {
        return attendanceJpaRepository.findByStudyId(studyId);
    }

    @Override
    public List<Attendance> findByStudyWeekIdIn(List<Long> studyWeekIds) {
        return attendanceJpaRepository.findByStudyWeekIdIn(studyWeekIds);
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
