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
    public static class Create {
        private final Long mainActivitiesId;
        private final Member member;
        private final String title;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final int year;
        private final int views;
        private final List<String> images;

        public static Create from(MainActivities mainActivities) {
            return Create.builder()
                    .mainActivitiesId(mainActivities.getMainActivitiesId())
                    .member(mainActivities.getMember())
                    .title(mainActivities.getTitle())
                    .startDate(mainActivities.getStartDate())
                    .endDate(mainActivities.getEndDate())
                    .year(mainActivities.getYear())
                    .views(mainActivities.getViews())
                    .images(mainActivities.getImages())
                    .build();
        }
    }

    /** 주요활동 게시판 게시물 연도별 조회 API */
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

    /** 주요활동 게시판 게시물 단일 조회 Response DTO */



}
