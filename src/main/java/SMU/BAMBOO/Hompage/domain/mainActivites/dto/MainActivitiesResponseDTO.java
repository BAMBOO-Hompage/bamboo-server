package SMU.BAMBOO.Hompage.domain.mainActivites.dto;


import SMU.BAMBOO.Hompage.domain.mainActivites.entity.MainActivities;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Schema(description = "주요활동 게시판 응답 DTO")
public class MainActivitiesResponseDTO {

    /** 주요활동 게시판 게시물 생성 Response DTO */
    @Getter
    @Builder
    @Schema(description = "주요활동 게시판 게시물 생성 응답 DTO")
    public static class Detail {
        private final Long mainActivitiesId;
        private final String memberName;
        private final String title;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final int year;
        private final int views;
        private final List<String> images;

        public static Detail from(MainActivities mainActivities) {
            return Detail.builder()
                    .mainActivitiesId(mainActivities.getMainActivitiesId())
                    .memberName(mainActivities.getMember().getName())
                    .title(mainActivities.getTitle())
                    .startDate(mainActivities.getStartDate())
                    .endDate(mainActivities.getEndDate())
                    .year(mainActivities.getYear())
                    .views(mainActivities.getViews())
                    .images(mainActivities.getImages() != null ? mainActivities.getImages() : List.of()) // null일 경우 빈 리스트 반환
                    .build();
        }
    }

    /** 주요활동 게시판 게시물 연도별 조회 Response DTO */
    @Getter
    @Builder
    @Schema(description = "주요활동 게시판 연도별 조회 응답 DTO")
    public static class ActivitiesByYearResponse {

        private final Long mainActivitiesId;
        private final String title;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final int year;
        private final int views;
        private final List<String> images;

        public static ActivitiesByYearResponse from(MainActivities mainActivities) {
            return ActivitiesByYearResponse.builder()
                    .mainActivitiesId(mainActivities.getMainActivitiesId())
                    .title(mainActivities.getTitle())
                    .startDate(mainActivities.getStartDate())
                    .endDate(mainActivities.getEndDate())
                    .year(mainActivities.getYear())
                    .views(mainActivities.getViews())
                    .images(mainActivities.getImages())
                    .build();
        }
    }


}
