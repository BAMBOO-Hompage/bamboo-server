package SMU.BAMBOO.Hompage.domain.attendance.repository;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceJpaRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudyWeekId(Long studyWeekId);
    @Query("SELECT a FROM Attendance a WHERE a.studyWeek.study.studyId = :studyId")
    List<Attendance> findByStudyId(@Param("studyId") Long studyId);
    List<Attendance> findByStudyWeekIdIn(List<Long> studyWeekIds);
    Optional<Attendance> findByStudyWeekAndMember(StudyWeek studyWeek, Member member);
}
