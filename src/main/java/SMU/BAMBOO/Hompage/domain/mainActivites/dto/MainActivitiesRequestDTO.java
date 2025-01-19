package SMU.BAMBOO.Hompage.domain.mainActivites.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "주요활동 게시판 요청 DTO")
public class MainActivitiesRequestDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @Schema(description = "주요활동 게시판 게시물 생성 요청 DTO")
    public static class Create {
        @Schema(description = "제목", example = "대회 참여")
        private String title;

        @Schema(description = "활동 시작 날짜", example = "2025-01-14")
        private LocalDate startDate;

        @Schema(description = "활동 완료 날짜", example = "2025-01-15")
        private LocalDate endDate;

        @Schema(description = "연도", example = "2025")
        private int year;

        @Schema(description = "이미지 파일 목록", example = "[]", required = false)
        private List<MultipartFile> images;

    }

    /**
     * 주요활동 게시판 게시물 수정 Request DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @Schema(description = "주요활동 게시판 수정 요청 DTO")
    public static class Update {
        @Schema(description = "제목", example = "수정된 제목")
        private String title;

        @Schema(description = "활동 시작 날짜", example = "2025-01-01")
        private LocalDate startDate;

        @Schema(description = "활동 종료 날짜", example = "2025-01-02")
        private LocalDate endDate;

        @Schema(description = "연도", example = "2025")
        private int year;

        @Schema(description = "이미지 파일 목록", example = "[]", required = false)
        private List<MultipartFile> images;

    }
}
