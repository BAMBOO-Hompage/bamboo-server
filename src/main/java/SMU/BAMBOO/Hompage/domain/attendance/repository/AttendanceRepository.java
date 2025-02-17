package SMU.BAMBOO.Hompage.domain.attendance.repository;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;

import java.util.List;

public interface AttendanceRepository {
    List<Attendance> findByStudyWeekId(Long studyWeekId);
    List<Attendance> findByStudyId(Long studyId);
    List<Attendance> findByStudyWeekIdIn(List<Long> studyWeekIds);
    void saveAll(List<Attendance> attendances);
}
