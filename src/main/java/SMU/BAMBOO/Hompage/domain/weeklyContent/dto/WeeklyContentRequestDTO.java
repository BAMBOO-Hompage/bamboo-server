package SMU.BAMBOO.Hompage.domain.weeklyContent.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "주차별 내용(커리큘럼) 관련 요청 DTO")
public class WeeklyContentRequestDTO {

    @Schema(description = "주차별 내용 생성 요청 DTO")
    public record Create(
            @Schema(description = "내용", example = "PY 1주차 커리큘럼") String content,
            @Schema(description = "주차", example = "1") int week,
            @Schema(description = "시작일", example = "2025-03-04") LocalDate startDate,
            @Schema(description = "종료일", example = "2025-03-11") LocalDate endDate,
            @Schema(description = "시작 페이지", example = "1") int startPage,
            @Schema(description = "끝 페이지", example = "100") int endPage
    ) {}

    @Schema(description = "주차별 내용 수정 요청 DTO")
    public record Update(
            @Schema(description = "내용", example = "[수정] PY 1주차 커리큘럼") String content,
            @Schema(description = "주차", example = "1") int week,
            @Schema(description = "시작일", example = "2025-03-04") LocalDate startDate,
            @Schema(description = "종료일", example = "2025-03-11") LocalDate endDate,
            @Schema(description = "시작 페이지", example = "50") int startPage,
            @Schema(description = "끝 페이지", example = "150") int endPage
    ) {}
}
