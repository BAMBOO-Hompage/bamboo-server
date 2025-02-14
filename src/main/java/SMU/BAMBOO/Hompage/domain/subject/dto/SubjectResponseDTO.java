package SMU.BAMBOO.Hompage.domain.subject.dto;

import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.domain.weeklyContent.dto.WeeklyContentResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "과목 응답 DTO")
public class SubjectResponseDTO {

    @Schema(description = "과목 생성 응답 DTO")
    public record Create(
            @Schema(description = "과목 ID") Long subjectId,
            @Schema(description = "과목 이름") String name,
            @Schema(description = "커리큘럼 유뮤") Boolean isBook,
            @Schema(description = "기수", example = "true") int batch
    ) {
        public static Create from(Subject subject) {
            return new Create(
                    subject.getSubjectId(),
                    subject.getName(),
                    subject.getIsBook(),
                    subject.getCohort().getBatch()
            );
        }
    }

    @Schema(description = "과목 수정 응답 DTO")
    public record Update(
            @Schema(description = "과목 ID") Long subjectId,
            @Schema(description = "과목 이름") String name,
            @Schema(description = "커리큘럼 유뮤") Boolean isBook,
            @Schema(description = "기수", example = "true") int batch
    ) {
        public static Update from(Subject subject) {
            return new Update(
                    subject.getSubjectId(),
                    subject.getName(),
                    subject.getIsBook(),
                    subject.getCohort().getBatch()
            );
        }
    }

    @Schema(description = "단일 과목 조회 응답 DTO")
    public record GetOne(
            @Schema(description = "과목 ID") Long subjectId,
            @Schema(description = "과목 이름") String name,
            @Schema(description = "커리큘럼 유뮤") Boolean isBook,
            @Schema(description = "기수", example = "true") int batch,
            @Schema(description = "주차별 내용") List<WeeklyContentResponseDTO.GetOne> weeklyContents
    ) {
        public static GetOne from(Subject subject) {
            return new GetOne(
                    subject.getSubjectId(),
                    subject.getName(),
                    subject.getIsBook(),
                    subject.getCohort().getBatch(),
                    subject.getWeeklyContents().stream()
                            .map(WeeklyContentResponseDTO.GetOne::from)
                            .toList()
            );
        }
    }
}
