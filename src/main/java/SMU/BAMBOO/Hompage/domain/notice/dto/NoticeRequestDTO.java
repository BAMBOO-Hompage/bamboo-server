package SMU.BAMBOO.Hompage.domain.notice.dto;

import SMU.BAMBOO.Hompage.domain.enums.NoticeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "공지사항 게시판 요청 DTO")
public class NoticeRequestDTO {

    @Schema(description = "공지사항 게시판 생성 요청 DTO")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @Schema(description = "제목", example = "공지사항 제목")
        @NotBlank(message = "제목은 필수 입력값입니다.")
        private String title;

        @Schema(description = "내용", example = "공지사항 내용")
        @NotBlank(message = "내용은 필수 입력값입니다.")
        private String content;

        @Schema(description = "타입", example = "대회 및 세미나")
        @NotNull(message = "공지사항 타입은 필수 입력값입니다.")
        private NoticeType type;
    }

    @Schema(description = "공지사항 게시판 수정 요청 DTO")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @Schema(description = "제목", example = "공지사항 제목 수정")
        private String title;

        @Schema(description = "내용", example = "공지사항 내용 수정")
        private String content;

        @Schema(description = "타입", example = "공지사항")
        private NoticeType type;
    }
}
