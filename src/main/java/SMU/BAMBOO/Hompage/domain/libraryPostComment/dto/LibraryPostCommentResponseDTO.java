package SMU.BAMBOO.Hompage.domain.libraryPostComment.dto;

import SMU.BAMBOO.Hompage.domain.libraryPostComment.entity.LibraryPostComment;
import SMU.BAMBOO.Hompage.domain.member.dto.MemberResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "자료 게시판 댓글 응답 DTO")
public class LibraryPostCommentResponseDTO {

    public record Create(
            Long commentId,
            MemberResponseDTO.MemberInfo member,
            String content
    ) {
        public static Create from(LibraryPostComment comment) {
            return new Create(
                    comment.getLibraryPostCommentId(),
                    MemberResponseDTO.MemberInfo.from(comment.getMember()),
                    comment.getContent()
            );
        }
    }

    public record GetOne(
            Long commentId,
            MemberResponseDTO.CommentMemberInfo member,
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
            MemberResponseDTO.CommentMemberInfo member = comment.isDeleted()
                    ? MemberResponseDTO.CommentMemberInfo.deletedUser()
                    : MemberResponseDTO.CommentMemberInfo.from(comment.getMember());

            return new GetOne(
                    comment.getLibraryPostCommentId(),
                    member,
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