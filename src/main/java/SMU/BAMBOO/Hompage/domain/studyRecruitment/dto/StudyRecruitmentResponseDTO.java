package SMU.BAMBOO.Hompage.domain.studyRecruitment.dto;

import SMU.BAMBOO.Hompage.domain.studyRecruitment.entity.StudyRecruitment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "스터디 모집 응답 DTO")
public class StudyRecruitmentResponseDTO {
    @Schema(description = "스터디 모집 생성 응답 DTO")
    public record Create(
            @Schema(description = "스터디 모집 ID") Long id,
            @Schema(description = "작성자 ID") Long writerId,
            @Schema(description = "작성자 이름") String writerName,
            @Schema(description = "제목") String title,
            @Schema(description = "내용") String content,
            @Schema(description = "모집 인원") int maxMembers,
            @Schema(description = "모집 마감일") LocalDate deadline,
            @Schema(description = "생성일") LocalDateTime createdAt,
            @Schema(description = "수정일") LocalDateTime updatedAt
    ) {
        public static Create from(StudyRecruitment sr) {
            return new Create(
                    sr.getId(),
                    sr.getWriterId(),
                    sr.getWriterName(),
                    sr.getTitle(),
                    sr.getContent(),
                    sr.getMaxMembers(),
                    sr.getDeadline(),
                    sr.getCreatedAt(),
                    sr.getModifiedAt()
            );
        }
    }

    @Schema(description = "스터디 모집 수정 응답 DTO")
    public record Update(
            @Schema(description = "스터디 모집 ID") Long id,
            @Schema(description = "제목") String title,
            @Schema(description = "내용") String content,
            @Schema(description = "모집 인원") int maxMembers,
            @Schema(description = "모집 마감일") LocalDate deadline,
            @Schema(description = "생성일") LocalDateTime createdAt,
            @Schema(description = "수정일") LocalDateTime updatedAt
    ) {
        public static Update from(StudyRecruitment sr) {
            return new Update(
                    sr.getId(),
                    sr.getTitle(),
                    sr.getContent(),
                    sr.getMaxMembers(),
                    sr.getDeadline(),
                    sr.getCreatedAt(),
                    sr.getModifiedAt()
            );
        }
    }

    @Schema(description = "스터디 모집 단건 조회 응답 DTO")
    public record GetOne(
            @Schema(description = "스터디 모집 ID") Long id,
            @Schema(description = "작성자 ID") Long writerId,
            @Schema(description = "작성자 이름") String writerName,
            @Schema(description = "제목") String title,
            @Schema(description = "내용") String content,
            @Schema(description = "모집 인원") int maxMembers,
            @Schema(description = "모집 마감일") LocalDate deadline,
            @Schema(description = "생성일") LocalDateTime createdAt,
            @Schema(description = "수정일") LocalDateTime updatedAt
    ) {
        public static GetOne from(StudyRecruitment sr) {
            return new GetOne(
                    sr.getId(),
                    sr.getWriterId(),
                    sr.getWriterName(),
                    sr.getTitle(),
                    sr.getContent(),
                    sr.getMaxMembers(),
                    sr.getDeadline(),
                    sr.getCreatedAt(),
                    sr.getModifiedAt()
            );
        }
    }
}
