package SMU.BAMBOO.Hompage.domain.libraryPost.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "알렉산드리아 도서관 요청 DTO")
public class LibraryPostRequestDTO {

    @Schema(description = "알렉산드리아 도서관 글 생성 요청 DTO")
    public record Create(
            @Schema(description = "논문 링크", example = "xxx.xxx") String link,
            @Schema(description = "연도", example = "2025") int year,
            @Schema(description = "논문 이름", example = "CV 논문") String paperName,
            @Schema(description = "주제", example = "CV") String topic,
            @Schema(description = "내용", example = "최신 논문에 대한 정리") String content,
            @Schema(description = "태그", example = "[\"CV\", \"AI\"]") List<String> tagNames
    ) {}

    @Schema(description = "알렉산드리아 도서관 글 수정 요청 DTO")
    public record Update(
            @Schema(description = "논문 링크", example = "xxx.xxx") String link,
            @Schema(description = "연도", example = "2025") int year,
            @Schema(description = "논문 이름", example = "[수정] CV 논문") String paperName,
            @Schema(description = "주제", example = "CV") String topic,
            @Schema(description = "내용", example = "[수정] 최신 논문에 대한 정리") String content,
            @Schema(description = "태그", example = "[\"CV\", \"AI\"]") List<String> tagNames
    ) {}

    @Schema(description = "알렉산드리아 도서관 글의 태그 초기화 요청 DTO")
    public record ResetTag(
            @Schema(description = "태그", example = "[\"ML\", \"AI\"]") List<String> tagNames
    ) {}
}

