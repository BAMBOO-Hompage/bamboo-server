package SMU.BAMBOO.Hompage.domain.libraryPostComment.dto;

import SMU.BAMBOO.Hompage.domain.libraryPostComment.entity.LibraryPostComment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "자료 게시판 댓글 응답 DTO")
public class LibraryPostCommentResponseDTO {

    public record Create(
            Long commentId,
            String writerName,
            String content
    ) {
        public static Create from(LibraryPostComment comment) {
            return new Create(
                    comment.getLibraryPostCommentId(),
                    comment.getWriterName(),
                    comment.getContent()
            );
        }
    }

    public record GetOne(
            Long commentId,
            Long memberId,
            String writerName,
            String writerMajor,
            String writerImageUrl,
            String content,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            List<GetOne> children
    ) {
        public static GetOne from(LibraryPostComment comment) {
            List<GetOne> children = comment.getChildren() != null ?
                    comment.getChildren().stream().map(GetOne::from).collect(Collectors.toList())
                    : List.of();

            String content = comment.isDeleted() ? "삭제된 댓글입니다." : comment.getContent();
            String writerName = comment.isDeleted()
                    ? "알 수 없음" : comment.getWriterName();
            String writerImageUrl = comment.isDeleted()
                    ? null : comment.getWriterImageUrl();

            return new GetOne(
                    comment.getLibraryPostCommentId(),
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

    @Schema(description = "자료 게시판 댓글 페이지네이션 응답 DTO")
    public record PagedGet(
            List<GetOne> content,
            int page,
            int size,
            int totalPages,
            long totalElements
    ) {
        public static PagedGet from(org.springframework.data.domain.Page<GetOne> pageResult) {
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