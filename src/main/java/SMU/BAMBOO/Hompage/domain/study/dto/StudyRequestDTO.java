package SMU.BAMBOO.Hompage.domain.study.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "스터디 요청 DTO")
public class StudyRequestDTO {

    @Schema(description = "스터디 생성 요청 DTO")
    public record Create(
            @Schema(description = "과목 ID", example = "1") Long subjectId,
            @Schema(description = "기수", example = "6") int cohort,
            @Schema(description = "커리큘럼 유무", example = "true") Boolean isBook,
            @Schema(description = "분반", example = "1") int section,
            @Schema(description = "스터디장", example = "202510777") String studyMaster,
            @Schema(description = "스터디원", example = "[\"202510770\", \"202510771\", \"202510772\"]") List<String> studyMembers
    ) {}

    @Schema(description = "스터디 수정 요청 DTO")
    public record Update(
            @Schema(description = "과목 ID", example = "1") Long subjectId,
            @Schema(description = "기수", example = "6") int cohort,
            @Schema(description = "커리큘럼 유무", example = "true") Boolean isBook,
            @Schema(description = "분반", example = "1") int section,
            @Schema(description = "스터디장", example = "202510777") String studyMaster,
            @Schema(description = "스터디원", example = "[\"202510770\", \"202510771\"]") List<String> studyMembers
    ) {}
}

