package SMU.BAMBOO.Hompage.domain.member.service;

import SMU.BAMBOO.Hompage.domain.member.dto.request.*;
import SMU.BAMBOO.Hompage.domain.member.dto.response.LoginResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MyPageResponse;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface MemberService {

    MemberResponse signUp(MemberSignUpDto request, BCryptPasswordEncoder encoder);
    LoginResponse login(MemberLoginDto request, HttpServletResponse response);
    Member getMember(String studentId);
    String logout(String accessToken);
    MyPageResponse updateProfile(Long memberId, UpdateProfileDto request);
    void updatePw(Long memberId, UpdatePwDto request);
    MemberResponse updateRole(Long currentMemberId, UpdateRoleDto request);
    MemberResponse testUpdateRole(TestUpdateRoleDto request);
}
