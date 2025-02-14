package SMU.BAMBOO.Hompage.domain.cohort.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기수 관련 요청 DTO")
public class CohortRequestDTO {

    @Schema(description = "기수 생성 요청 DTO")
    public record Create(
            @Schema(description = "몇 기인지", example = "6") int batch,
            @Schema(description = "연도", example = "2025") int year,
            @Schema(description = "학기", example = "1학기") Boolean isFirstSemester
    ) {}

    @Schema(description = "기수 수정 요청 DTO")
    public record Update(
            @Schema(description = "몇 기인지", example = "6") int batch,
            @Schema(description = "연도", example = "2025") int year,
            @Schema(description = "학기", example = "1학기") Boolean isFirstSemester
    ) {}
}
