package SMU.BAMBOO.Hompage.domain.attendance.repository;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;

import java.util.List;

public interface AttendanceRepository {
    List<Attendance> findByStudyWeekId(Long studyWeekId);
    List<Attendance> findByStudyWeek_Study(Study study);
    void saveAll(List<Attendance> attendances);
}
