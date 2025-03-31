package SMU.BAMBOO.Hompage.domain.libraryPostComment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "알렉산드리아 댓글 요청 DTO")
public class LibraryPostCommentRequestDTO {

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Schema(description = "댓글 작성 요청 DTO")
    public static class Create {
        @NotBlank(message = "댓글 내용은 비어있을 수 없음")
        private String content;

        @Schema(description = "부모 댓글 ID", example = "12", nullable = true)
        private Long parentId;
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Schema(description = "댓글 수정 요청 DTO")
    public static class Update {
        @NotBlank(message = "댓글 내용은 비어있을 수 없음")
        private String content;
    }
}
