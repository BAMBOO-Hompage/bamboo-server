package SMU.BAMBOO.Hompage.domain.attendance.service;

import SMU.BAMBOO.Hompage.domain.attendance.dto.AttendanceRequestDTO;
import SMU.BAMBOO.Hompage.domain.attendance.dto.AttendanceResponseDTO;
import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.attendance.repository.AttendanceRepository;
import SMU.BAMBOO.Hompage.domain.enums.AttendanceStatus;
import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.repository.MemberStudyRepository;
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
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudyRepository studyRepository;
    private final StudyWeekRepository studyWeekRepository;
    private final MemberRepository memberRepository;
    private final MemberStudyRepository memberStudyRepository;

    /**
     * 출석 정보 등록
     */
    @Transactional
    public void markAttendance(AttendanceRequestDTO.MarkAttendance request) {
        Study study = studyRepository.findById(request.studyId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_EXIST));

        // StudyWeek 찾기 or 생성
        StudyWeek studyWeek = studyWeekRepository.findByStudyAndWeek(study, request.week())
                .orElseGet(() -> studyWeekRepository.save(
                        StudyWeek.builder()
                                .study(study)
                                .week(request.week())
                                .build()
                ));

        // 회원 출석 정보 저장 / 수정
        List<Attendance> attendances = request.attendances().stream().map(att -> {
            Member member = memberRepository.findByStudentId(att.studentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
            AttendanceStatus status = AttendanceStatus.from(att.status());

            // 해당 회원이 스터디에 속해 있는지 검증
            boolean isMemberInStudy = memberStudyRepository.existsByStudyIdAndMemberId(study.getStudyId(), member.getMemberId());
            if (!isMemberInStudy) {
                throw new CustomException(ErrorCode.USER_NOT_IN_STUDY);
            }

            // 기존 출석 정보가 있는지 확인
            Optional<Attendance> existingAttendance = attendanceRepository.findByStudyWeekAndMember(studyWeek, member);

            // 출석 정보가 이미 존재하면 수정, 없으면 새로 생성
            if (existingAttendance.isPresent()) {
                Attendance attendance = existingAttendance.get();
                attendance.updateStatus(status);
                return attendance;
            } else {
                return Attendance.create(studyWeek, member, status);
            }
        }).toList();
        attendanceRepository.saveAll(attendances);
    }

    /**
     * 특정 스터디의 출석부 조회
     */
    @Transactional
    public AttendanceResponseDTO.GetAttendanceBoard getAttendanceBoard(AttendanceRequestDTO.MarkAttendance request) {
        Study study = studyRepository.findById(request.studyId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_EXIST));

        // 해당 스터디의 모든 주차 조회
        List<StudyWeek> studyWeeks = studyWeekRepository.findByStudyOrderByWeekAsc(study);

        // 모든 출석 정보 조회
        List<Long> studyWeekIds = studyWeeks.stream().map(StudyWeek::getId).toList();
        List<Attendance> allAttendances = attendanceRepository.findByStudyWeekIdIn(studyWeekIds);

        // 주차별로 출석 정보 매핑
        Map<Integer, List<Attendance>> weekAttendanceMap = new TreeMap<>();
        for (StudyWeek week : studyWeeks) {
            List<Attendance> attendances = allAttendances.stream()
                    .filter(att -> att.getStudyWeek().getId().equals(week.getId()))
                    .toList();
            weekAttendanceMap.put(week.getWeek(), attendances);
        }

        // DTO 변환
        List<AttendanceResponseDTO.GetWeekAttendance> weekAttendanceList = weekAttendanceMap.entrySet().stream()
                .map(entry -> AttendanceResponseDTO.GetWeekAttendance.from(entry.getKey(), entry.getValue()))
                .toList();

        return AttendanceResponseDTO.GetAttendanceBoard.from(request.studyId(), weekAttendanceList);
    }

    public List<AttendanceResponseDTO.GetOne> getAttendanceByWeek(Long studyWeekId) {
        List<Attendance> attendances = attendanceRepository.findByStudyWeekId(studyWeekId);
        return attendances.stream()
                .map(AttendanceResponseDTO.GetOne::from)
                .toList();
    }
}
