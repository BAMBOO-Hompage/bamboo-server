package SMU.BAMBOO.Hompage.domain.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스터디 정리본 관련 요청 DTO")
public class InventoryRequestDTO {

    @Schema(description = "스터디 정리본 생성 요청 DTO")
    public record Create(
            @Schema(description = "스터디 ID", example = "1") Long studyId,
            @Schema(description = "제목", example = "1주차 CV 스터디 정리") String title,
            @Schema(description = "내용", example = "이것 저것 배웠습니다~") String content,
            @Schema(description = "스터디 주차", example = "1") int week
    ) {}

    @Schema(description = "스터디 정리본 수정 요청 DTO")
    public record Update(
            @Schema(description = "제목", example = "CV 스터디 정리") String title,
            @Schema(description = "내용", example = "수정했습니다~") String content,
            @Schema(description = "스터디 주차", example = "1") int week
    ) {}
}
