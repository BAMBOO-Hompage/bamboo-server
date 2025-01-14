package SMU.BAMBOO.Hompage.domain.member.dto.response;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private String studentId;
    private Role role;

    public static LoginResponse from(Member member) {
        return LoginResponse.builder()
                .studentId(member.getStudentId())
                .role(member.getRole())
                .build();
    }
}
