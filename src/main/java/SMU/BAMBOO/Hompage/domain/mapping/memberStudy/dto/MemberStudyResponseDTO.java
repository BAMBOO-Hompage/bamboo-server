package SMU.BAMBOO.Hompage.domain.mapping.memberStudy.dto;

import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.entity.MemberStudy;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import SMU.BAMBOO.Hompage.domain.study.dto.StudyResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "MemberStudy 응답 DTO")
public class MemberStudyResponseDTO {

    @Schema(description = "MemberStudy 조회 DTO")
    public record Detail(
            MemberResponse member,
            StudyResponseDTO.GetOne study,
            int oCount,
            int xCount,
            double attendanceRate
    ) {
        public static  Detail from(MemberStudy memberStudy) {
            int totalCount = memberStudy.getOCount() + memberStudy.getXCount();
            double attendanceRate = (totalCount == 0) ? 0.0 : (double) memberStudy.getOCount() / totalCount * 100;

            return new Detail(
                    MemberResponse.from(memberStudy.getMember()),
                    StudyResponseDTO.GetOne.from(memberStudy.getStudy()),
                    memberStudy.getOCount(),
                    memberStudy.getXCount(),
                    attendanceRate
            );
        }
    }
}
