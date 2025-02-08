package SMU.BAMBOO.Hompage.domain.weeklyContent.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주차별 내용(커리큘럼) 관련 요청 DTO")
public class WeeklyContentRequestDTO {

    @Schema(description = "주차별 내용 생성 요청 DTO")
    public record Create(
            @Schema(description = "과목 ID", example = "1") Long subjectId,
            @Schema(description = "내용", example = "PY 1주차 커리큘럼") String content,
            @Schema(description = "주차", example = "1") int week
    ) {}

    @Schema(description = "주차별 내용 수정 요청 DTO")
    public record Update(
            @Schema(description = "내용", example = "[수정] PY 1주차 커리큘럼") String content,
            @Schema(description = "주차", example = "1") int week
    ) {}
}
