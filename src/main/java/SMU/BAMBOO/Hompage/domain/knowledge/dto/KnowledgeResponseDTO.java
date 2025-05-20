package SMU.BAMBOO.Hompage.domain.knowledge.dto;

import SMU.BAMBOO.Hompage.domain.enums.KnowledgeType;
import SMU.BAMBOO.Hompage.domain.knowledge.entity.Knowledge;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "지식공유 게시판 응답 DTO")
public class KnowledgeResponseDTO {
    @Schema(description = "지식공유 게시판 생성 응답 DTO")
    public record Create(
            @Schema(description = "지식공유 게시판 ID") Long knowledgeId,
            @Schema(description = "작성자 ID") Long writerId,
            @Schema(description = "작성자 이름") String writerName,
            @Schema(description = "제목") String title,
            @Schema(description = "내용") String content,
            @Schema(description = "게시글 유형(RESOURCES, INSIGHTS, CAREERS)") KnowledgeType type,
            @Schema(description = "이미지 리스트") List<String> images,
            @Schema(description = "파일 리스트") List<String> files,
            @Schema(description = "생성일")LocalDateTime createdAt,
            @Schema(description = "수정일")LocalDateTime updatedAt
            ) {
        public static KnowledgeResponseDTO.Create from(Knowledge knowledge) {
            return new KnowledgeResponseDTO.Create(
                    knowledge.getKnowledgeId(),
                    knowledge.getWriterId(),
                    knowledge.getWriterName(),
                    knowledge.getTitle(),
                    knowledge.getContent(),
                    knowledge.getType(),
                    knowledge.getImages() != null ? knowledge.getImages() : new ArrayList<>(),
                    knowledge.getFiles() != null ? knowledge.getFiles() : new ArrayList<>(),
                    knowledge.getCreatedAt(),
                    knowledge.getModifiedAt()
            );
        }
    }

    @Schema(description = "지식공유 게시판 수정 응답 DTO")
    public record Update (
            @Schema(description = "지식공유 게시판 ID") Long knowledgeId,
            @Schema(description = "[수정] 제목") String title,
            @Schema(description = "[수정] 내용") String content,
            @Schema(description = "게시글 유형(RESOURCES, INSIGHTS, CAREERS)") KnowledgeType type,
            @Schema(description = "이미지 리스트") List<String> images,
            @Schema(description = "파일 리스트") List<String> files,
            @Schema(description = "생성일")LocalDateTime createdAt,
            @Schema(description = "수정일")LocalDateTime updatedAt
    ) {
        public static KnowledgeResponseDTO.Update from(Knowledge knowledge) {
            return new KnowledgeResponseDTO.Update(
                    knowledge.getKnowledgeId(),
                    knowledge.getTitle(),
                    knowledge.getContent(),
                    knowledge.getType(),
                    knowledge.getImages() != null ? knowledge.getImages() : new ArrayList<>(),
                    knowledge.getFiles() != null ? knowledge.getFiles() : new ArrayList<>(),
                    knowledge.getCreatedAt(),
                    knowledge.getModifiedAt()
            );
        }
    }

    @Schema(description = "지식공유 게시판 단건 조회 응답 DTO")
    public record GetOne(
            @Schema(description = "지식공유 게시판 ID") Long knowledgeId,
            @Schema(description = "작성자 ID") Long writerId,
            @Schema(description = "작성자 이름") String writerName,
            @Schema(description = "제목") String title,
            @Schema(description = "내용") String content,
            @Schema(description = "게시글 유형(RESOURCES, INSIGHTS, CAREERS)") KnowledgeType type,
            @Schema(description = "조회수") int views,
            @Schema(description = "이미지 리스트") List<String> images,
            @Schema(description = "파일 리스트") List<String> files,
            @Schema(description = "댓글 개수") int commentCount,
            @Schema(description = "생성일")LocalDateTime createdAt,
            @Schema(description = "수정일")LocalDateTime updatedAt
    ) {
        public static KnowledgeResponseDTO.GetOne from(Knowledge knowledge) {
            return new KnowledgeResponseDTO.GetOne(
                    knowledge.getKnowledgeId(),
                    knowledge.getWriterId(),
                    knowledge.getWriterName(),
                    knowledge.getTitle(),
                    knowledge.getContent(),
                    knowledge.getType(),
                    knowledge.getViews(),
                    knowledge.getImages() != null ? knowledge.getImages() : new ArrayList<>(),
                    knowledge.getFiles() != null ? knowledge.getFiles() : new ArrayList<>(),
                    knowledge.getKnowledgeComments() != null ? knowledge.getKnowledgeComments().size() : 0,
                    knowledge.getCreatedAt(),
                    knowledge.getModifiedAt()
            );
        }
    }
}
