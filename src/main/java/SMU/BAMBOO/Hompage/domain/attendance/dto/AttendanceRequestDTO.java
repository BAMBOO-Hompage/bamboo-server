package SMU.BAMBOO.Hompage.domain.attendance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "출석 요청 DTO")
public class AttendanceRequestDTO {

    @Schema(description = "주차별 출석 기록 요청 DTO")
    public record MarkAttendance(
            @Schema(description = "스터디 ID", example = "1") Long studyId,
            @Schema(description = "주차 (Week)", example = "2") int week,
            @Schema(description = "출석 정보 리스트", example = "[{\"memberId\": 1, \"status\": \"출석\"}, {\"memberId\": 2, \"status\": \"결석\"}]")
            List<MemberAttendance> attendances
    ) {}

    @Schema(description = "개별 회원 출석 정보")
    public record MemberAttendance(
            @Schema(description = "회원 ID", example = "3") Long memberId,
            @Schema(description = "출석 상태 (출석, 결석)", example = "출석") String status
    ) {}

}
