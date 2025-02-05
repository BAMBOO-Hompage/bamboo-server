
package SMU.BAMBOO.Hompage.domain.knowledgeComment.dto;

import SMU.BAMBOO.Hompage.domain.knowledgeComment.entity.KnowledgeComment;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "지식공유 게시판 댓글 응답 DTO")
public class KnowledgeCommentResponse{

    @Schema(description = "지식공유 게시판 댓글 생성 응답 DTO")
    public record Create(
            @Schema(description = "댓글 ID") Long commentId,
            @Schema(description = "작성한 멤버 정보") MemberResponse member,
            @Schema(description = "댓글 내용") String content
    ) {
        public static KnowledgeCommentResponse.Create from(KnowledgeComment comment) {
            return new KnowledgeCommentResponse.Create(
                    comment.getKnowledgeCommentId(),
                    MemberResponse.from(comment.getMember()),
                    comment.getContent()
            );
        }
    }

    @Schema(description = "지식공유 게시판 댓글 수정 응답 DTO")
    public record Update(
            @Schema(description = "댓글 ID") Long commentId,
            @Schema(description = "댓글 내용") String content
    ) {
        public static KnowledgeCommentResponse.Update from(KnowledgeComment comment) {
            return new KnowledgeCommentResponse.Update(
                    comment.getKnowledgeCommentId(),
                    comment.getContent()
            );
        }
    }

    @Schema(description = "지식공유 게시판 댓글 조회 응답 DTO")
    public record GetOne(
            @Schema(description = "댓글 ID") Long commentId,
            @Schema(description = "작성한 멤버 정보") MemberResponse member,
            @Schema(description = "댓글 내용") String content,
            @Schema(description = "작성일") LocalDateTime createdAt,
            @Schema(description = "수정일") LocalDateTime modifiedAt
    ) {
        public static KnowledgeCommentResponse.GetOne from(KnowledgeComment comment) {
            return new KnowledgeCommentResponse.GetOne(
                    comment.getKnowledgeCommentId(),
                    MemberResponse.from(comment.getMember()),
                    comment.getContent(),
                    comment.getCreatedAt(),
                    comment.getModifiedAt()
            );
        }
    }
}
