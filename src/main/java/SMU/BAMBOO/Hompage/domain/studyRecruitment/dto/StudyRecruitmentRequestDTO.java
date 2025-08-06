package SMU.BAMBOO.Hompage.domain.studyRecruitment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "스터디 모집 요청 DTO")
public class StudyRecruitmentRequestDTO {

    @Schema(description = "스터디 모집 생성 요청 DTO")
    public record Create(
            @Schema(description = "제목", example = "Spring 스터디 모집") String title,
            @Schema(description = "내용", example = "함께 공부할 분 모집합니다.") String content,
            @Schema(description = "모집 인원", example = "5") int maxMembers,
            @Schema(description = "모집 마감일", example = "2025-08-31") LocalDate deadline
    ) {}

    @Schema(description = "스터디 모집 수정 요청 DTO")
    public record Update(
            @Schema(description = "제목", example = "[수정] Spring 스터디 모집") String title,
            @Schema(description = "내용", example = "[수정] 함께 공부할 분 모집합니다.") String content,
            @Schema(description = "모집 인원", example = "6") int maxMembers,
            @Schema(description = "모집 마감일", example = "2025-09-10") LocalDate deadline
    ) {}
}
