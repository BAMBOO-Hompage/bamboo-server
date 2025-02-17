package SMU.BAMBOO.Hompage.domain.attendance.repository;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceJpaRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudyWeekId(Long studyWeekId);
    List<Attendance> findByStudyWeek_Study(Study study);
}
