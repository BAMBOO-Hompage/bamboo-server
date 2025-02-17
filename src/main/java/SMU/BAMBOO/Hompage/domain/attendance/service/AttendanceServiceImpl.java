package SMU.BAMBOO.Hompage.domain.attendance.service;

import SMU.BAMBOO.Hompage.domain.attendance.dto.AttendanceRequestDTO;
import SMU.BAMBOO.Hompage.domain.attendance.dto.AttendanceResponseDTO;
import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.attendance.repository.AttendanceRepository;
import SMU.BAMBOO.Hompage.domain.enums.AttendanceStatus;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.study.repository.StudyRepository;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;
import SMU.BAMBOO.Hompage.domain.studyWeek.repository.StudyWeekRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudyRepository studyRepository;
    private final StudyWeekRepository studyWeekRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void markAttendance(AttendanceRequestDTO.MarkAttendance request) {
        Study study = studyRepository.findById(request.studyId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_EXIST));

        // StudyWeek 찾기 or 생성
        StudyWeek studyWeek = studyWeekRepository.findByStudyAndWeekNumber(study, request.week())
                .orElseGet(() -> studyWeekRepository.save(
                        StudyWeek.builder()
                                .study(study)
                                .weekNumber(request.week())
                                .build()
                ));

        // 회원 출석 정보 저장
        List<Attendance> attendances = request.attendances().stream().map(att -> {
            Member member = memberRepository.findById(att.memberId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

            AttendanceStatus status = AttendanceStatus.from(att.status());
            return Attendance.create(studyWeek, member, status);
        }).toList();

        attendanceRepository.saveAll(attendances);
    }

    public List<AttendanceResponseDTO.GetOne> getAttendanceByWeek(Long studyWeekId) {
        List<Attendance> attendances = attendanceRepository.findByStudyWeekId(studyWeekId);
        return attendances.stream()
                .map(AttendanceResponseDTO.GetOne::from)
                .toList();
    }
}
