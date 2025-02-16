package SMU.BAMBOO.Hompage.domain.weeklyContent.dto;

import SMU.BAMBOO.Hompage.domain.weeklyContent.entity.WeeklyContent;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "주차별 내용(커리큘럼) 관련 응답 DTO")
public class WeeklyContentResponseDTO {

    @Schema(description = "주차별 내용 생성 응답 DTO")
    public record Create(
            @Schema(description = "주차별 내용 ID") Long weeklyContentId,
            @Schema(description = "과목 이름") String subjectName,
            @Schema(description = "내용") String content,
            @Schema(description = "주차") int week,
            @Schema(description = "시작일") LocalDate startDate,
            @Schema(description = "종료일") LocalDate endDate,
            @Schema(description = "시작 페이지") int startPage,
            @Schema(description = "끝 페이지") int endPage
    ) {
        public static Create from(WeeklyContent weeklyContent) {
            return new Create(
                    weeklyContent.getWeeklyContentId(),
                    weeklyContent.getSubject().getName(),
                    weeklyContent.getContent(),
                    weeklyContent.getWeek(),
                    weeklyContent.getStartDate(),
                    weeklyContent.getEndDate(),
                    weeklyContent.getStartPage(),
                    weeklyContent.getEndPage()
            );
        }
    }

    @Schema(description = "주차별 내용 수정 응답 DTO")
    public record Update(
            @Schema(description = "주차별 내용 ID") Long weeklyContentId,
            @Schema(description = "내용") String content,
            @Schema(description = "주차") int week,
            @Schema(description = "시작일") LocalDate startDate,
            @Schema(description = "종료일") LocalDate endDate,
            @Schema(description = "시작 페이지") int startPage,
            @Schema(description = "끝 페이지") int endPage
    ) {
        public static Update from(WeeklyContent weeklyContent) {
            return new Update(
                    weeklyContent.getWeeklyContentId(),
                    weeklyContent.getContent(),
                    weeklyContent.getWeek(),
                    weeklyContent.getStartDate(),
                    weeklyContent.getEndDate(),
                    weeklyContent.getStartPage(),
                    weeklyContent.getEndPage()
            );
        }
    }

    @Schema(description = "주차별 내용 생성 응답 DTO")
    public record GetOne(
            @Schema(description = "주차별 내용 ID") Long weeklyContentId,
            @Schema(description = "과목 이름") String subjectName,
            @Schema(description = "내용") String content,
            @Schema(description = "주차") int week,
            @Schema(description = "시작일") LocalDate startDate,
            @Schema(description = "종료일") LocalDate endDate,
            @Schema(description = "시작 페이지") int startPage,
            @Schema(description = "끝 페이지") int endPage
    ) {
        public static GetOne from(WeeklyContent weeklyContent) {
            return new GetOne(
                    weeklyContent.getWeeklyContentId(),
                    weeklyContent.getSubject().getName(),
                    weeklyContent.getContent(),
                    weeklyContent.getWeek(),
                    weeklyContent.getStartDate(),
                    weeklyContent.getEndDate(),
                    weeklyContent.getStartPage(),
                    weeklyContent.getEndPage()
            );
        }
    }
}
