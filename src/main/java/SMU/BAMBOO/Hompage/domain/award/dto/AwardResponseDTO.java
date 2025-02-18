package SMU.BAMBOO.Hompage.domain.award.dto;

import SMU.BAMBOO.Hompage.domain.award.entity.Award;
import SMU.BAMBOO.Hompage.domain.study.dto.StudyResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "명예의 전당 관련 응답 DTO")
public class AwardResponseDTO {

    @Schema(description = "명예의 전당 생성 응답 DTO")
    public record Create(
            @Schema(description = "Award Id") Long awardId,
            @Schema(description = "스터디 정보") StudyResponseDTO.GetForAward study,
            @Schema(description = "제목") String title,
            @Schema(description = "주차") int week,
            @Schema(description = "시작 기간") LocalDate startDate,
            @Schema(description = "마감 기간") LocalDate endDate
    ) {
        public static Create from(Award award) {
            return new Create(
                    award.getAwardId(),
                    StudyResponseDTO.GetForAward.from(award.getInventory().getStudy()),
                    award.getTitle(),
                    award.getWeek(),
                    award.getStartDate(),
                    award.getEndDate()
            );
        }
    }

    @Schema(description = "명예의 전당 수정 응답 DTO")
    public record Update(
            @Schema(description = "스터디 정보") StudyResponseDTO.GetForAward study,
            @Schema(description = "제목") String title,
            @Schema(description = "기수") int batch,
            @Schema(description = "주차") int week,
            @Schema(description = "시작 기간") LocalDate startDate,
            @Schema(description = "마감 기간") LocalDate endDate
    ) {
        public static Update from(Award award) {
            return new Update(
                    StudyResponseDTO.GetForAward.from(award.getInventory().getStudy()),
                    award.getTitle(),
                    award.getBatch(),
                    award.getWeek(),
                    award.getStartDate(),
                    award.getEndDate()
            );
        }
    }

    @Schema(description = "명예의 전당 조회 응답 DTO")
    public record GetOne(
            @Schema(description = "Award Id") Long awardId,
            @Schema(description = "스터디 정보") StudyResponseDTO.GetForAward study,
            @Schema(description = "제목") String title,
            @Schema(description = "기수") int batch,
            @Schema(description = "주차") int week,
            @Schema(description = "시작 기간") LocalDate startDate,
            @Schema(description = "마감 기간") LocalDate endDate
    ) {
        public static GetOne from(Award award) {
            return new GetOne(
                    award.getAwardId(),
                    StudyResponseDTO.GetForAward.from(award.getInventory().getStudy()),
                    award.getTitle(),
                    award.getBatch(),
                    award.getWeek(),
                    award.getStartDate(),
                    award.getEndDate()
            );
        }
    }
}
