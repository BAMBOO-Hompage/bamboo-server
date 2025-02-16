package SMU.BAMBOO.Hompage.domain.subject.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "과목 요청 DTO")
public class SubjectRequestDTO {

    @Schema(description = "과목 생성 요청 DTO")
    public record Create(
            @Schema(description = "과목 이름", example = "PY") String name,
            @Schema(description = "책이름", example = "혼공자바") String bookName,
            @Schema(description = "커리큘럼 유무", example = "true") Boolean isBook,
            @Schema(description = "기수", example = "true") int batch
    ) {}

    @Schema(description = "과목 수정 요청 DTO")
    public record Update(
            @Schema(description = "과목 이름", example = "PY") String name,
            @Schema(description = "책이름", example = "혼공데분") String bookName,
            @Schema(description = "커리큘럼 유무", example = "true") Boolean isBook,
            @Schema(description = "기수", example = "true") int batch
    ) {}
}
