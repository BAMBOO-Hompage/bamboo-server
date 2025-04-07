package SMU.BAMBOO.Hompage.domain.award.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "명예의 전당 요청 DTO")
public class AwardRequestDTO {

    @Schema(description = "명예의 전당 생성 요청 DTO")
    public record Create(
            @Schema(description = "주차", example = "1") int week,
            @Schema(description = "기수", example = "6") int batch,
            @Schema(description = "스터디 정리본 ID", example = "1") Long inventoryId,
            @Schema(description = "과목 ID", example = "1") Long subjectId,
            @Schema(description = "분반", example = "1") int section,
            @Schema(description = "선발된 회원 ID", example = "1") Long memberId
    ) {}

    @Schema(description = "명예의 전당 수정 요청 DTO")
    public record Update(
            @Schema(description = "주차", example = "2") int week,
            @Schema(description = "기수", example = "6") int batch,
            @Schema(description = "스터디 정리본 ID", example = "1") Long inventoryId,
            @Schema(description = "과목 ID", example = "1") Long subjectId,
            @Schema(description = "분반", example = "2") int section,
            @Schema(description = "선발된 회원 ID", example = "2") Long memberId
    ) {}
}
