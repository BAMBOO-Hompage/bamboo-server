package SMU.BAMBOO.Hompage.domain.knowledgeComment.dto;

import SMU.BAMBOO.Hompage.domain.knowledgeComment.entity.KnowledgeComment;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "지식공유 게시판 댓글 응답 DTO")
public class KnowledgeCommentResponseDTO {

    @Schema(description = "지식공유 게시판 댓글 생성 응답 DTO")
    public record Create(
            @Schema(description = "댓글 ID") Long commentId,
            @Schema(description = "작성자 ID") Long writerId,
            @Schema(description = "작성자 이름") String writerName,
            @Schema(description = "댓글 내용") String content
    ) {
        public static Create from(KnowledgeComment comment) {
            return new Create(
                    comment.getKnowledgeCommentId(),
                    comment.getWriterId(),
                    comment.getWriterName(),
                    comment.getContent()
            );
        }
    }

    @Schema(description = "지식공유 게시판 댓글 수정 응답 DTO")
    public record Update(
            @Schema(description = "댓글 ID") Long commentId,
            @Schema(description = "댓글 내용") String content
    ) {
        public static Update from(KnowledgeComment comment) {
            return new Update(
                    comment.getKnowledgeCommentId(),
                    comment.getContent()
            );
        }
    }

    @Schema(description = "지식공유 게시판 댓글 조회 응답 DTO (대댓글 포함)")
    public record GetOne(
            @Schema(description = "댓글 ID") Long commentId,
            @Schema(description = "작성자 ID") Long writerId,
            @Schema(description = "작성자 이름") String writerName,
            @Schema(description = "작성자 전공") String writerMajor,
            @Schema(description = "작성자 이미지 URL") String writerImageUrl,
            @Schema(description = "댓글 내용") String content,
            @Schema(description = "작성일") LocalDateTime createdAt,
            @Schema(description = "수정일") LocalDateTime modifiedAt,
            @Schema(description = "대댓글 목록") List<GetOne> children
    ) {
        public static GetOne from(KnowledgeComment comment) {
            List<GetOne> children = comment.getChildren() != null
                    ? comment.getChildren().stream()
                    .map(GetOne::from)
                    .collect(Collectors.toList())
                    : List.of();

            String content = comment.isDeleted() ? "삭제된 댓글입니다." : comment.getContent();
            String writerName = comment.isDeleted()
                    ? "알 수 없음" : comment.getWriterName();
            String writerImageUrl = comment.isDeleted()
                    ? null : comment.getWriterImageUrl();

            return new GetOne(
                    comment.getKnowledgeCommentId(),
                    comment.getWriterId(),
                    writerName,
                    comment.getWriterMajor(),
                    writerImageUrl,
                    content,
                    comment.getCreatedAt(),
                    comment.getModifiedAt(),
                    children
            );
        }

    }

    @Schema(description = "댓글 페이지네이션 응답 DTO")
    public record PagedGet(
            @Schema(description = "댓글 리스트") List<GetOne> content,
            @Schema(description = "현재 페이지 (1부터 시작)") int page,
            @Schema(description = "페이지 크기") int size,
            @Schema(description = "전체 페이지 수") int totalPages,
            @Schema(description = "전체 댓글 수") long totalElements
    ) {
        public static PagedGet from(Page<GetOne> pageResult) {
            return new PagedGet(
                    pageResult.getContent(),
                    pageResult.getNumber() + 1,
                    pageResult.getSize(),
                    pageResult.getTotalPages(),
                    pageResult.getTotalElements()
            );
        }
    }
}
