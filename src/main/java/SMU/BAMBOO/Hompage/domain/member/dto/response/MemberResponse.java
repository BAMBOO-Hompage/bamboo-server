package SMU.BAMBOO.Hompage.domain.member.dto.response;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {

    private Long id;
    private String studentId;
    private String email;
    private String name;
    private String major;
    private String phone;
    private Role role;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getMemberId())
                .studentId(member.getStudentId())
                .email(member.getEmail())
                .name(member.getName())
                .major(member.getMajor())
                .phone(member.getPhone())
                .role(member.getRole())
                .build();
    }
}
