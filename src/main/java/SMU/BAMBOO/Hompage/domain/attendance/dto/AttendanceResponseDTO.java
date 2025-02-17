package SMU.BAMBOO.Hompage.domain.attendance.dto;

import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "출석부 응답 DTO")
public class AttendanceResponseDTO {

    @Schema(description = "출석 기록 생성 응답 DTO")
    public record GetOne(
            @Schema(description = "출석 ID") Long attendanceId,
            @Schema(description = "스터디 주차 ID") Long studyWeekId,
            @Schema(description = "회원 학번") String memberId,
            @Schema(description = "회원 이름") String memberName,
            @Schema(description = "출석 상태 (출석, 결석)") String status
    ) {
        public static GetOne from(Attendance attendance) {
            return new GetOne(
                    attendance.getId(),
                    attendance.getStudyWeek().getId(),
                    attendance.getMember().getStudentId(),
                    attendance.getMember().getName(),
                    attendance.getStatus().getDescription()
            );
        }
    }

    @Schema(description = "주차별 출석부 조회 응답 DTO")
    public record GetWeekAttendance(
            @Schema(description = "스터디 주차 ID") Long studyWeekId,
            @Schema(description = "출석 목록") List<GetOne> attendances
    ) {
        public static GetWeekAttendance from(Long studyWeekId, List<Attendance> attendanceList) {
            return new GetWeekAttendance(
                    studyWeekId,
                    attendanceList.stream().map(GetOne::from).toList()
            );
        }
    }
}

