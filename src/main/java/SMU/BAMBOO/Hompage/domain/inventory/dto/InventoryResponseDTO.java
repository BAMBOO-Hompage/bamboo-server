package SMU.BAMBOO.Hompage.domain.inventory.dto;

import SMU.BAMBOO.Hompage.domain.award.dto.AwardResponseDTO;
import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.study.dto.StudyResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스터디 정리본 관련 응답 DTO")
public class InventoryResponseDTO {

    @Schema(description = "스터디 정리본 생성 응답 DTO")
    public record Create(
            @Schema(description = "작성한 멤버 이름") String writerName,
            @Schema(description = "관련된 스터디 정보") StudyResponseDTO.GetForInventory study,
            @Schema(description = "정리본 제목") String title,
            @Schema(description = "몇 주차 정리본인지") int week,
            @Schema(description = "PDF 파일의 URL") String fileUrl
    ) {
        public static Create from(Inventory inventory) {
            return new Create(
                    inventory.getWriterName(),
                    StudyResponseDTO.GetForInventory.from(inventory.getStudy()),
                    inventory.getTitle(),
                    inventory.getWeek(),
                    inventory.getFileUrl()
            );
        }
    }

    @Schema(description = "스터디 정리본 수정 응답 DTO")
    public record Update(
            @Schema(description = "작성한 멤버 이름") String writerName,
            @Schema(description = "관련된 스터디 정보") StudyResponseDTO.GetForInventory study,
            @Schema(description = "정리본 제목") String title,
            @Schema(description = "PDF 파일의 URL") String fileUrl,
            @Schema(description = "몇 주차 정리본인지") int week
    ) {
        public static Update from(Inventory inventory) {
            return new Update(
                    inventory.getWriterName(),
                    StudyResponseDTO.GetForInventory.from(inventory.getStudy()),
                    inventory.getTitle(),
                    inventory.getFileUrl(),
                    inventory.getWeek()
            );
        }
    }

    @Schema(description = "스터디 정리본 단건 조회 응답 DTO")
    public record GetOne(
            @Schema(description = "정리본 ID") Long inventoryId,
            @Schema(description = "작성자 ID") Long writerId,
            @Schema(description = "작성자 학번") String writerStudentId,
            @Schema(description = "작성자 이름") String writerName,
            @Schema(description = "작성자 프로필 URL") String writerImageUrl,
            @Schema(description = "관련된 스터디 정보") StudyResponseDTO.GetForInventory study,
            @Schema(description = "정리본 제목") String title,
            @Schema(description = "정리본 내용") String content,
            @Schema(description = "몇 주차 정리본인지") int week,
            @Schema(description = "weekly best 여부") Boolean isWeeklyBest,
            @Schema(description = "PDF 파일의 URL") String fileUrl,
            @Schema(description = "수상 내역") AwardResponseDTO.GetOne award
    ) {
        public static GetOne from(Inventory inventory) {
            return new GetOne(
                    inventory.getInventoryId(),
                    inventory.getWriterId(),
                    inventory.getWriterStudentId(),
                    inventory.getWriterName(),
                    inventory.getWriterImageUrl(),
                    StudyResponseDTO.GetForInventory.from(inventory.getStudy()),
                    inventory.getTitle(),
                    inventory.getContent(),
                    inventory.getWeek(),
                    inventory.getIsWeeklyBest(),
                    inventory.getFileUrl(),
                    AwardResponseDTO.GetOne.from(inventory.getAward())
            );
        }
    }
}

