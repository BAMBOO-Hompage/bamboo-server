package SMU.BAMBOO.Hompage.domain.attendance.repository;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceJpaRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudyWeekId(Long studyWeekId);
    Optional<Attendance> findByStudyWeekAndMember(StudyWeek studyWeek, Member member);
}
