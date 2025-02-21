package SMU.BAMBOO.Hompage.domain.study.dto;

import SMU.BAMBOO.Hompage.domain.attendance.dto.AttendanceResponseDTO;
import SMU.BAMBOO.Hompage.domain.attendance.entity.Attendance;
import SMU.BAMBOO.Hompage.domain.cohort.dto.CohortResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberInStudyResponse;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "스터디 응답 DTO")
public class StudyResponseDTO {

    @Schema(description = "스터디 생성 응답 DTO")
    public record Create(
            @Schema(description = "스터디 ID") Long studyId,
            @Schema(description = "팀명", example = "에그타르트") String teamName,
            @Schema(description = "과목 이름") String subjectName,
            @Schema(description = "기수 정보") CohortResponseDTO.GetOne cohort,
            @Schema(description = "커리큘럼 유무") Boolean isBook,
            @Schema(description = "분반") int section,
            @Schema(description = "스터디장") MemberInStudyResponse studyMaster,
            @Schema(description = "스터디원") List<MemberInStudyResponse> studyMembers
    ) {
        public static Create from(Study study) {
            return new Create(
                    study.getStudyId(),
                    study.getTeamName(),
                    study.getSubject().getName(),
                    CohortResponseDTO.GetOne.from(study.getCohort()),
                    study.getIsBook(),
                    study.getSection(),
                    MemberInStudyResponse.from(study.getStudyMaster()),
                    study.getMemberStudies().stream()
                            .map(memberStudy -> MemberInStudyResponse.from(memberStudy.getMember()))
                            .toList()
            );
        }
    }

    @Schema(description = "스터디 수정 응답 DTO")
    public record Update(
            @Schema(description = "팀명", example = "에그타르트") String teamName,
            @Schema(description = "과목 이름") String subjectName,
            @Schema(description = "기수 정보") CohortResponseDTO.GetOne cohort,
            @Schema(description = "커리큘럼 유무") Boolean isBook,
            @Schema(description = "분반") int section,
            @Schema(description = "스터디장") MemberInStudyResponse studyMaster,
            @Schema(description = "스터디원") List<MemberInStudyResponse> studyMembers
    ) {
        public static Update from(Study study) {
            return new Update(
                    study.getTeamName(),
                    study.getSubject().getName(),
                    CohortResponseDTO.GetOne.from(study.getCohort()),
                    study.getIsBook(),
                    study.getSection(),
                    MemberInStudyResponse.from(study.getStudyMaster()),
                    study.getMemberStudies().stream()
                            .map(memberStudy -> MemberInStudyResponse.from(memberStudy.getMember()))
                            .toList()
            );
        }
    }

    @Schema(description = "스터디 단건 조회 응답 DTO")
    public record GetOne(
            @Schema(description = "스터디 ID") Long studyId,
            @Schema(description = "팀명", example = "에그타르트") String teamName,
            @Schema(description = "과목 이름") String subjectName,
            @Schema(description = "기수 정보") CohortResponseDTO.GetOne cohort,
            @Schema(description = "커리큘럼 유무") Boolean isBook,
            @Schema(description = "분반") int section,
            @Schema(description = "스터디장") MemberInStudyResponse studyMaster,
            @Schema(description = "스터디원") List<MemberInStudyResponse> studyMembers
    ) {
        public static GetOne from(Study study) {
            return new GetOne(
                    study.getStudyId(),
                    study.getTeamName(),
                    study.getSubject().getName(),
                    CohortResponseDTO.GetOne.from(study.getCohort()),
                    study.getIsBook(),
                    study.getSection(),
                    MemberInStudyResponse.from(study.getStudyMaster()),
                    study.getMemberStudies().stream()
                            .map(memberStudy -> MemberInStudyResponse.from(memberStudy.getMember()))
                            .toList()
            );
        }
    }

    @Schema(description = "출석 관련 스터디 단건 조회 응답 DTO")
    public record GetOneWithAttendance(
            @Schema(description = "스터디 ID") Long studyId,
            @Schema(description = "팀명", example = "에그타르트") String teamName,
            @Schema(description = "과목 이름") String subjectName,
            @Schema(description = "기수 정보") CohortResponseDTO.GetOne cohort,
            @Schema(description = "커리큘럼 유무") Boolean isBook,
            @Schema(description = "분반") int section,
            @Schema(description = "스터디장") MemberInStudyResponse studyMaster,
            @Schema(description = "스터디원") List<MemberInStudyResponse> studyMembers,
            @Schema(description = "출석 정보") List<AttendanceResponseDTO.GetOne> attendances
    ) {
        public static GetOneWithAttendance from(Study study, List<Attendance> attendances) {
            return new GetOneWithAttendance(
                    study.getStudyId(),
                    study.getTeamName(),
                    study.getSubject().getName(),
                    CohortResponseDTO.GetOne.from(study.getCohort()),
                    study.getIsBook(),
                    study.getSection(),
                    MemberInStudyResponse.from(study.getStudyMaster()),
                    study.getMemberStudies().stream()
                            .map(memberStudy -> MemberInStudyResponse.from(memberStudy.getMember()))
                            .toList(),
                    attendances.stream()
                            .map(AttendanceResponseDTO.GetOne::from).toList()
            );
        }
    }

    @Schema(description = "명예의 전당 관련 스터디 단건 조회 응답 DTO")
    public record GetForAward(
            @Schema(description = "스터디 ID") Long studyId,
            @Schema(description = "팀명", example = "에그타르트") String teamName,
            @Schema(description = "과목 이름") String subjectName,
            @Schema(description = "기수") int batch,
            @Schema(description = "분반") int section,
            @Schema(description = "스터디장") MemberInStudyResponse studyMaster,
            @Schema(description = "스터디원") List<MemberInStudyResponse> studyMembers
    ) {
        public static GetForAward from(Study study) {
            return new GetForAward(
                    study.getStudyId(),
                    study.getTeamName(),
                    study.getSubject().getName(),
                    study.getCohort().getBatch(),
                    study.getSection(),
                    MemberInStudyResponse.from(study.getStudyMaster()),
                    study.getMemberStudies().stream()
                            .map(memberStudy -> MemberInStudyResponse.from(memberStudy.getMember()))
                            .toList()
            );
        }
    }
}
