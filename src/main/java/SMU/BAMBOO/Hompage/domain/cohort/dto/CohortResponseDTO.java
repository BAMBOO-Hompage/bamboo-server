package SMU.BAMBOO.Hompage.domain.cohort.dto;

import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;
import SMU.BAMBOO.Hompage.domain.subject.dto.SubjectResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "기수 관련 응답 DTO")
public class CohortResponseDTO {

    @Schema(description = "기수 생성 응답 DTO")
    public record Create(
            @Schema(description = "기수 ID") Long cohortId,
            @Schema(description = "몇 기인지") int batch,
            @Schema(description = "연도") int year,
            @Schema(description = "학기") Boolean isFirstSemester,
            @Schema(description = "상태") String status
    ) {
        public static Create from(Cohort cohort) {
            return new Create(
                    cohort.getCohortId(),
                    cohort.getBatch(),
                    cohort.getYear(),
                    cohort.isFirstSemester(),
                    cohort.getStatus().getDescription()
            );
        }
    }

    @Schema(description = "기수 단일 조회 응답 DTO")
    public record GetOne(
            @Schema(description = "기수 ID") Long cohortId,
            @Schema(description = "몇 기인지") int batch,
            @Schema(description = "연도") int year,
            @Schema(description = "학기") Boolean isFirstSemester,
            @Schema(description = "활동 상태") String status,
            @Schema(description = "과목 리스트") List<SubjectResponseDTO.GetBasic> subjects
    ) {
        public static GetOne from(Cohort cohort) {
            return new GetOne(
                    cohort.getCohortId(),
                    cohort.getBatch(),
                    cohort.getYear(),
                    cohort.isFirstSemester(),
                    cohort.getStatus().getDescription(),
                    cohort.getSubjects().stream()
                            .map(SubjectResponseDTO.GetBasic::from)
                            .toList()
            );
        }
    }

    @Schema(description = "기수 리스트 조회 응답 DTO")
    public record GetAll(
            @Schema(description = "기수 리스트") List<GetOne> cohorts
    ) {
        public static GetAll from(List<Cohort> cohortList) {
            return new GetAll(cohortList.stream().map(GetOne::from).toList());
        }
    }
}
