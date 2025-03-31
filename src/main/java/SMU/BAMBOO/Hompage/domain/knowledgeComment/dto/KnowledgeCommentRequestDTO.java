package SMU.BAMBOO.Hompage.domain.knowledgeComment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "지식공유 게시판 댓글 요청 DTO")
public class KnowledgeCommentRequestDTO {

    @Schema(description = "지식공유 게시판 댓글 작성 요청 DTO")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        @Schema(description = "댓글 내용", example = "댓글 내용")
        @NotBlank(message = "댓글 내용은 비어있을 수 없음")
        private String content;
        @Schema(
                description = "부모 댓글 ID (대댓글 작성 시에만 사용하며, 최상위 부모 댓글일 경우 보내지 마세요)",
                example = "12",
                nullable = true
        )
        private Long parentId;
    }

    @Schema(description = "지식공유 게시판 댓글 수정 요청 DTO")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {

        @Schema(description = "댓글 내용", example = "댓글 내용 수정")
        @NotBlank(message = "댓글 내용은 비어있을 수 없음")
        private String content;
    }
}
