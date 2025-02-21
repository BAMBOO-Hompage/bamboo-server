package SMU.BAMBOO.Hompage.domain.member.dto.response;

import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInStudyResponse {

    private String studentId;
    private String name;

    public static MemberInStudyResponse from(Member member) {
        return MemberInStudyResponse.builder()
                .studentId(member.getStudentId())
                .name(member.getName())
                .build();
    }
}
