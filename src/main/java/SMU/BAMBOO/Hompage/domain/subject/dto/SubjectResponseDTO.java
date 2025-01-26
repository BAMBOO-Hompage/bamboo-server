package SMU.BAMBOO.Hompage.domain.subject.dto;

import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.domain.weeklyContent.entity.WeeklyContent;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "과목 응답 DTO")
public class SubjectResponseDTO {

    @Schema(description = "과목 생성 응답 DTO")
    public record Create(
            Long subjectId,
            String name
    ) {
        public static Create from(Subject subject) {
            return new Create(
                    subject.getSubjectId(),
                    subject.getName().name()
            );
        }
    }

    @Schema(description = "과목 수정 응답 DTO")
    public record Update(
            Long subjectId,
            String name
    ) {
        public static Update from(Subject subject) {
            return new Update(
                    subject.getSubjectId(),
                    subject.getName().name()
            );
        }
    }

    @Schema(description = "단일 과목 조회 응답 DTO")
    public record GetOne(
            Long subjectId,
            String name,
            List<WeeklyContent> weeklyContents
    ) {
        public static GetOne from(Subject subject) {
            return new GetOne(
                    subject.getSubjectId(),
                    subject.getName().name(),
                    subject.getWeeklyContents()
            );
        }
    }
}
