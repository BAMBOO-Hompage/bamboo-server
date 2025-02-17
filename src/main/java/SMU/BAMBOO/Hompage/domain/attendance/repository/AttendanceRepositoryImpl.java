package SMU.BAMBOO.Hompage.domain.attendance.repository;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
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
}
