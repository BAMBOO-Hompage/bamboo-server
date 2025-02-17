package SMU.BAMBOO.Hompage.domain.attendance.repository;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {

    private final AttendanceJpaRepository attendanceJpaRepository;

    @Override
    public List<Attendance> findByStudyWeekId(Long studyWeekId) {
        return attendanceJpaRepository.findByStudyWeekId(studyWeekId);
    }

    @Override
    public List<Attendance> findByStudyWeek_Study(Study study) {
        return attendanceJpaRepository.findByStudyWeek_Study(study);
    }

    @Override
    public void saveAll(List<Attendance> attendances) {
        attendanceJpaRepository.saveAll(attendances);
    }
}
