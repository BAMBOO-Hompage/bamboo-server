package SMU.BAMBOO.Hompage.domain.weeklyContent.dto;

import SMU.BAMBOO.Hompage.domain.weeklyContent.entity.WeeklyContent;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주차별 내용(커리큘럼) 관련 응답 DTO")
public class WeeklyContentResponseDTO {

    @Schema(description = "주차별 내용 생성 응답 DTO")
    public record Create(
            @Schema(description = "주차별 내용 ID") Long weeklyContentId,
            @Schema(description = "과목 이름") String subjectName,
            @Schema(description = "내용") String content,
            @Schema(description = "주차") int week
    ) {
        public static Create from(WeeklyContent weeklyContent) {
            return new Create(
                    weeklyContent.getWeeklyContentId(),
                    weeklyContent.getSubject().getName(),
                    weeklyContent.getContent(),
                    weeklyContent.getWeek()
            );
        }
    }

    @Schema(description = "주차별 내용 수정 응답 DTO")
    public record Update(
            @Schema(description = "주차별 내용 ID") Long weeklyContentId,
            @Schema(description = "내용") String content,
            @Schema(description = "주차") int week
    ) {
        public static Update from(WeeklyContent weeklyContent) {
            return new Update(
                    weeklyContent.getWeeklyContentId(),
                    weeklyContent.getContent(),
                    weeklyContent.getWeek()
            );
        }
    }

    @Schema(description = "주차별 내용 생성 응답 DTO")
    public record GetOne(
            @Schema(description = "주차별 내용 ID") Long weeklyContentId,
            @Schema(description = "과목 이름") String subjectName,
            @Schema(description = "내용") String content,
            @Schema(description = "주차") int week
    ) {
        public static GetOne from(WeeklyContent weeklyContent) {
            return new GetOne(
                    weeklyContent.getWeeklyContentId(),
                    weeklyContent.getSubject().getName(),
                    weeklyContent.getContent(),
                    weeklyContent.getWeek()
            );
        }
    }
}
