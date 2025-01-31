package SMU.BAMBOO.Hompage.domain.inventory.dto;

import SMU.BAMBOO.Hompage.domain.award.entity.Award;
import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import SMU.BAMBOO.Hompage.domain.study.dto.StudyResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스터디 정리본 관련 응답 DTO")
public class InventoryResponseDTO {

    @Schema(description = "스터디 정리본 생성 응답 DTO")
    public record Create(
            @Schema(description = "정리본 ID")
            Long inventoryId,

            @Schema(description = "작성한 멤버 정보")
            MemberResponse member,

            @Schema(description = "관련된 스터디 정보")
            StudyResponseDTO.GetOne study,

            @Schema(description = "정리본 제목")
            String title,

            @Schema(description = "몇 주차 정리본인지")
            int week
    ) {
        public static Create from(Inventory inventory) {
            return new Create(
                    inventory.getInventoryId(),
                    MemberResponse.from(inventory.getMember()),
                    StudyResponseDTO.GetOne.from(inventory.getStudy()),
                    inventory.getTitle(),
                    inventory.getWeek()
            );
        }
    }

    @Schema(description = "스터디 정리본 수정 응답 DTO")
    public record Update(
            @Schema(description = "정리본 ID")
            Long inventoryId,

            @Schema(description = "작성한 멤버 정보")
            MemberResponse member,

            @Schema(description = "관련된 스터디 정보")
            StudyResponseDTO.GetOne study,

            @Schema(description = "정리본 제목")
            String title,

            @Schema(description = "몇 주차 정리본인지")
            int week
    ) {
        public static Update from(Inventory inventory) {
            return new Update(
                    inventory.getInventoryId(),
                    MemberResponse.from(inventory.getMember()),
                    StudyResponseDTO.GetOne.from(inventory.getStudy()),
                    inventory.getTitle(),
                    inventory.getWeek()
            );
        }
    }

    @Schema(description = "스터디 정리본 단건 조회 응답 DTO")
    public record GetOne(
            @Schema(description = "정리본 ID")
            Long inventoryId,

            @Schema(description = "작성한 멤버 정보")
            MemberResponse member,

            @Schema(description = "관련된 스터디 정보")
            StudyResponseDTO.GetOne study,

            @Schema(description = "정리본 제목")
            String title,

            @Schema(description = "정리본 내용")
            String content,

            @Schema(description = "몇 주차 정리본인지")
            int week,

            @Schema(description = "수상 내역")
            Award award
    ) {
        public static GetOne from(Inventory inventory) {
            return new GetOne(
                    inventory.getInventoryId(),
                    MemberResponse.from(inventory.getMember()),
                    StudyResponseDTO.GetOne.from(inventory.getStudy()),
                    inventory.getTitle(),
                    inventory.getContent(),
                    inventory.getWeek(),
                    inventory.getAward() // FIXME 나중에 DTO 로 변경 가능성
            );
        }
    }
}

