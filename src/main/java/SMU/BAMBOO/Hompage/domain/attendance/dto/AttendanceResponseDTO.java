package SMU.BAMBOO.Hompage.domain.attendance.dto;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "출석부 응답 DTO")
public class AttendanceResponseDTO {

    @Schema(description = "출석 기록 생성 응답 DTO")
    public record GetOne(
            @Schema(description = "출석 ID") Long attendanceId,
            @Schema(description = "주차") int week,
            @Schema(description = "회원 학번") String studentId,
            @Schema(description = "출석 상태 (출석, 결석)") String status
    ) {
        public static GetOne from(Attendance attendance) {
            return new GetOne(
                    attendance.getId(),
                    attendance.getStudyWeek().getWeek(),
                    attendance.getMember().getStudentId(),
                    attendance.getStatus().getDescription()
            );
        }
    }

    @Schema(description = "주차별 출석부 조회 응답 DTO")
    public record GetWeekAttendance(
            @Schema(description = "주차 (Week)") int week,
            @Schema(description = "출석 목록") List<MemberAttendance> members
    ) {
        public static GetWeekAttendance from(int week, List<Attendance> attendanceList) {
            return new GetWeekAttendance(
                    week,
                    attendanceList.stream().map(MemberAttendance::from).toList()
            );
        }
    }

    @Schema(description = "회원 출석 정보 DTO")
    public record MemberAttendance(
            @Schema(description = "회원 학번") String memberId,
            @Schema(description = "출석 상태 (출석, 결석)") String status
    ) {
        public static MemberAttendance from(Attendance attendance) {
            return new MemberAttendance(
                    attendance.getMember().getStudentId(),
                    attendance.getStatus().getDescription()
            );
        }
    }

    @Schema(description = "스터디 전체 출석부 조회 응답 DTO")
    public record GetAttendanceBoard(
            @Schema(description = "스터디 ID") Long studyId,
            @Schema(description = "출석 정보") List<GetWeekAttendance> attendance
    ) {
        public static GetAttendanceBoard from(Long studyId, List<GetWeekAttendance> attendance) {
            return new GetAttendanceBoard(studyId, attendance);
        }
    }
}

