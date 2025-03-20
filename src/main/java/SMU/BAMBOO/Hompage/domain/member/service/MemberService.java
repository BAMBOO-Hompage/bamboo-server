package SMU.BAMBOO.Hompage.domain.member.service;

import SMU.BAMBOO.Hompage.domain.member.dto.MemberRequestDTO;
import SMU.BAMBOO.Hompage.domain.member.dto.response.LoginResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MyPageResponse;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface MemberService {

    MemberResponse signUp(MemberRequestDTO.SignUp request, BCryptPasswordEncoder encoder);
    LoginResponse login(MemberRequestDTO.Login request, HttpServletResponse response);
    Member getMember(String studentId);
    Page<MemberResponse> getMembers(int page, int size);
    String logout(String accessToken);
    MyPageResponse updateProfile(Long memberId, MemberRequestDTO.UpdateProfile request);
    MyPageResponse deleteProfileImage(Long memberId);
    void updatePw(Long memberId, MemberRequestDTO.UpdatePw request);
    void resetPw(MemberRequestDTO.ResetPw request);
    MemberResponse updateRole(Long currentMemberId, MemberRequestDTO.UpdateRole request);
    void deactivateMember(Long memberId);
}
