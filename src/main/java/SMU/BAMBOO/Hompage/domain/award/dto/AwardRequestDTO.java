package SMU.BAMBOO.Hompage.domain.award.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "명예의 전당 요청 DTO")
public class AwardRequestDTO {

    @Schema(description = "명예의 전당 생성 요청 DTO")
    public record Create(
            @Schema(description = "주차", example = "1") int week,
            @Schema(description = "제목", example = "명예의 전당") String title,
            @Schema(description = "기수", example = "6") int batch,
            @Schema(description = "시작 기간", example = "2025-03-14") LocalDate startDate,
            @Schema(description = "마감 기간", example = "2025-03-21") LocalDate endDate,
            @Schema(description = "스터디 정리본 ID", example = "1") Long inventoryId,
            @Schema(description = "과목 ID", example = "1") Long subjectId,
            @Schema(description = "분반", example = "1") int section,
            @Schema(description = "선발된 회원 ID", example = "1") Long memberId
    ) {}

    @Schema(description = "명예의 전당 수정 요청 DTO")
    public record Update(
            @Schema(description = "주차", example = "2") int week,
            @Schema(description = "제목", example = "명예의 전당") String title,
            @Schema(description = "기수", example = "6") int batch,
            @Schema(description = "시작 기간", example = "2025-03-14") LocalDate startDate,
            @Schema(description = "마감 기간", example = "2025-03-21") LocalDate endDate,
            @Schema(description = "스터디 정리본 ID", example = "1") Long inventoryId,
            @Schema(description = "과목 ID", example = "1") Long subjectId,
            @Schema(description = "분반", example = "2") int section,
            @Schema(description = "선발된 회원 ID", example = "2") Long memberId
    ) {}
}
