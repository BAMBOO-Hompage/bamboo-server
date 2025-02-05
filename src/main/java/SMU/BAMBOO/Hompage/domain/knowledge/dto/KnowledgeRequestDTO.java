package SMU.BAMBOO.Hompage.domain.knowledge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "지식공유 게시판 요청 DTO")
public class KnowledgeRequestDTO {

    @Schema(description = "지식공유 게시판 생성 요청 DTO")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @Schema(description = "제목", example = "Python 지식 공유")
        private String title;

        @Schema(description = "내용", example = "~Python 지식 방출 중~")
        private String content;

        @Schema(description = "타입", example = "RESOURCES")
        private String type;
    }


    @Schema(description = "지식공유 게시판 수정 요청 DTO")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @Schema(description = "제목", example = "[수정] Python 지식 공유")
        private String title;

        @Schema(description = "내용", example = "[수정] Python 지식 방출 중~")
        private String content;

        @Schema(description = "타입", example = "RESOURCES")
        private String type;
    }
}
