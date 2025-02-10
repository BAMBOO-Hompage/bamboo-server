package SMU.BAMBOO.Hompage.domain.knowledge.dto;

import SMU.BAMBOO.Hompage.domain.enums.KnowledgeType;
import SMU.BAMBOO.Hompage.domain.knowledge.entity.Knowledge;
import SMU.BAMBOO.Hompage.domain.knowledgeComment.dto.KnowledgeCommentResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "지식공유 게시판 응답 DTO")
public class KnowledgeResponseDTO {
    @Schema(description = "지식공유 게시판 생성 응답 DTO")
    public record Create(
            @Schema(description = "지식공유 게시판 ID") Long knowledgeId,
            @Schema(description = "작성한 멤버 정보") MemberResponse member,
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
                    MemberResponse.from(knowledge.getMember()),
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
            @Schema(description = "작성한 멤버 정보") MemberResponse member,
            @Schema(description = "제목") String title,
            @Schema(description = "내용") String content,
            @Schema(description = "게시글 유형(RESOURCES, INSIGHTS, CAREERS)") KnowledgeType type,
            @Schema(description = "조회수") int views,
            @Schema(description = "이미지 리스트") List<String> images,
            @Schema(description = "파일 리스트") List<String> files,
            @Schema(description = "댓글 리스트") List<KnowledgeCommentResponse.GetOne> comments,
            @Schema(description = "생성일")LocalDateTime createdAt,
            @Schema(description = "수정일")LocalDateTime updatedAt
    ) {
        public static KnowledgeResponseDTO.GetOne from(Knowledge knowledge) {
            return new KnowledgeResponseDTO.GetOne(
                    knowledge.getKnowledgeId(),
                    MemberResponse.from(knowledge.getMember()),
                    knowledge.getTitle(),
                    knowledge.getContent(),
                    knowledge.getType(),
                    knowledge.getViews(),
                    knowledge.getImages() != null ? knowledge.getImages() : new ArrayList<>(),
                    knowledge.getFiles() != null ? knowledge.getFiles() : new ArrayList<>(),
                    knowledge.getKnowledgeComments().stream()
                            .map(KnowledgeCommentResponse.GetOne::from)
                            .collect(Collectors.toList()),
                    knowledge.getCreatedAt(),
                    knowledge.getModifiedAt()
            );
        }
    }
}
