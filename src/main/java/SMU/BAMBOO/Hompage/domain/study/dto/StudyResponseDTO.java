package SMU.BAMBOO.Hompage.domain.study.dto;

import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "스터디 응답 DTO")
public class StudyResponseDTO {

    @Schema(description = "스터디 생성 응답 DTO")
    public record Create(
            Long studyId,
            String subjectName,
            int cohort,
            Boolean isBook,
            int section,
            String studyMaster,
            List<String> studyMembers
    ) {
        public static Create from(Study study) {
            return new Create(
                    study.getStudyId(),
                    study.getSubject().getName(),
                    study.getCohort(),
                    study.getIsBook(),
                    study.getSection(),
                    study.getStudyMaster(),
                    study.getMemberStudies().stream()
                            .map(memberStudy -> memberStudy.getMember().getStudentId())
                            .toList()
            );
        }
    }

    @Schema(description = "스터디 수정 응답 DTO")
    public record Update(
            String subjectName,
            int cohort,
            Boolean isBook,
            int section,
            String studyMaster,
            List<String> studyMembers
    ) {
        public static Update from(Study study) {
            return new Update(
                    study.getSubject().getName(),
                    study.getCohort(),
                    study.getIsBook(),
                    study.getSection(),
                    study.getStudyMaster(),
                    study.getMemberStudies().stream()
                            .map(memberStudy -> memberStudy.getMember().getStudentId())
                            .toList()
            );
        }
    }

    @Schema(description = "스터디 단건 조회 응답 DTO")
    public record GetOne(
            Long studyId,
            String subjectName,
            int cohort,
            Boolean isBook,
            int section,
            String studyMaster,
            List<String> studyMembers
    ) {
        public static GetOne from(Study study) {
            return new GetOne(
                    study.getStudyId(),
                    study.getSubject().getName(),
                    study.getCohort(),
                    study.getIsBook(),
                    study.getSection(),
                    study.getStudyMaster(),
                    study.getMemberStudies().stream()
                            .map(memberStudy -> memberStudy.getMember().getStudentId())
                            .toList()
            );
        }
    }
}
