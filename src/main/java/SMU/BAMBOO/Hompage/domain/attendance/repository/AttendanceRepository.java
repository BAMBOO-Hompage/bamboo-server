package SMU.BAMBOO.Hompage.domain.attendance.repository;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository {
    List<Attendance> findByStudyWeekId(Long studyWeekId);
    List<Attendance> findByStudyId(Long studyId);
    List<Attendance> findByStudyWeekIdIn(List<Long> studyWeekIds);
    void saveAll(List<Attendance> attendances);
    Optional<Attendance> findByStudyWeekAndMember(StudyWeek studyWeek, Member member);
}
