package SMU.BAMBOO.Hompage.domain.award.dto;

import SMU.BAMBOO.Hompage.domain.award.entity.Award;
import SMU.BAMBOO.Hompage.domain.study.dto.StudyResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "명예의 전당 관련 응답 DTO")
public class AwardResponseDTO {

    @Schema(description = "명예의 전당 생성 응답 DTO")
    public record Create(
            @Schema(description = "Award Id") Long awardId,
            @Schema(description = "스터디 정보") StudyResponseDTO.GetForAward study,
            @Schema(description = "중간/기말 여부") boolean isMidterm
    ) {
        public static Create from(Award award) {
            return new Create(
                    award.getAwardId(),
                    StudyResponseDTO.GetForAward.from(award.getInventory().getStudy()),
                    award.isMidterm()
            );
        }
    }

    @Schema(description = "명예의 전당 수정 응답 DTO")
    public record Update(
            @Schema(description = "스터디 정보") StudyResponseDTO.GetForAward study,
            @Schema(description = "기수") int batch,
            @Schema(description = "중간/기말 여부") boolean isMidterm
    ) {
        public static Update from(Award award) {
            return new Update(
                    StudyResponseDTO.GetForAward.from(award.getInventory().getStudy()),
                    award.getBatch(),
                    award.isMidterm()
            );
        }
    }

    @Schema(description = "명예의 전당 조회 응답 DTO")
    public record GetOne(
            @Schema(description = "Award Id") Long awardId,
            @Schema(description = "작성자 이름") String writerName,
            @Schema(description = "작성자 Id") Long memberId,
            @Schema(description = "작성자 프로필 URL") String writerProfileUrl,
            @Schema(description = "스터디 정보") StudyResponseDTO.GetForAward study,
            @Schema(description = "기수") int batch,
            @Schema(description = "중간/기말 여부") boolean isMidterm
    ) {
        public static GetOne from(Award award) {
            if (award == null) {
                return null;
            }

            return new GetOne(
                    award.getAwardId(),
                    award.getInventory().getWriterName(),
                    award.getInventory().getWriterId(),
                    award.getInventory().getWriterImageUrl(),
                    StudyResponseDTO.GetForAward.from(award.getInventory().getStudy()),
                    award.getBatch(),
                    award.isMidterm()
            );
        }
    }
}
