package SMU.BAMBOO.Hompage.domain.knowledgeComment.dto;

import SMU.BAMBOO.Hompage.domain.knowledgeComment.entity.KnowledgeComment;
import SMU.BAMBOO.Hompage.domain.member.dto.MemberResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "지식공유 게시판 댓글 응답 DTO")
public class KnowledgeCommentResponseDTO {

    @Schema(description = "지식공유 게시판 댓글 생성 응답 DTO")
    public record Create(
            @Schema(description = "댓글 ID") Long commentId,
            @Schema(description = "작성한 멤버 정보") MemberResponseDTO.MemberInfo member,
            @Schema(description = "댓글 내용") String content
    ) {
        public static Create from(KnowledgeComment comment) {
            return new Create(
                    comment.getKnowledgeCommentId(),
                    MemberResponseDTO.MemberInfo.from(comment.getMember()),
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
            @Schema(description = "작성한 멤버 정보") MemberResponseDTO.CommentMemberInfo member,
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

            String content = comment.isDeleted()
                    ? "삭제된 댓글입니다."
                    : comment.getContent();

            MemberResponseDTO.CommentMemberInfo memberInfo = comment.isDeleted()
                    ? MemberResponseDTO.CommentMemberInfo.deletedUser()
                    : MemberResponseDTO.CommentMemberInfo.from(comment.getMember());

            return new GetOne(
                    comment.getKnowledgeCommentId(),
                    memberInfo,
                    content,
                    comment.getCreatedAt(),
                    comment.getModifiedAt(),
                    children
            );
        }

    }
}
