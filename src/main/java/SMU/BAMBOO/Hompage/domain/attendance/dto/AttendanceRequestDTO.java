package SMU.BAMBOO.Hompage.domain.attendance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "출석 요청 DTO")
public class AttendanceRequestDTO {

    @Schema(description = "주차별 출석 기록 요청 DTO")
    public record MarkAttendance(
            @Schema(description = "스터디 ID", example = "1") Long studyId,
            @Schema(description = "주차 (Week)", example = "2") int week,
            @Schema(description = "출석 정보 리스트", example = "[{\"studentId\": \"202510777\", \"status\": \"출석\"}, {\"studentId\": \"202510770\", \"status\": \"결석\"}]")
            List<MemberAttendance> attendances
    ) {}

    @Schema(description = "개별 회원 출석 정보")
    public record MemberAttendance(
            @Schema(description = "학번", example = "202510777") String studentId,
            @Schema(description = "출석 상태 (출석, 결석)", example = "출석") String status
    ) {}

}
