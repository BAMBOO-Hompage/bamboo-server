package SMU.BAMBOO.Hompage.domain.member.service;

import SMU.BAMBOO.Hompage.domain.member.dto.MemberRequestDTO;
import SMU.BAMBOO.Hompage.domain.member.dto.MemberResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface MemberService {

    interface AuthenticationService {
        MemberResponseDTO.MemberInfo signUp(MemberRequestDTO.SignUp request, BCryptPasswordEncoder encoder);

        MemberResponseDTO.Login login(MemberRequestDTO.Login request, HttpServletResponse response);

        String logout(String accessToken);
    }

    interface MemberInfoService {
        Member getMember(String studentId);

        Page<MemberResponseDTO.MemberInfo> getMembers(int page, int size);

        MemberResponseDTO.MyPage updateProfile(Long memberId, MemberRequestDTO.UpdateProfile request);

        MemberResponseDTO.MyPage deleteProfileImage(Long memberId);

        void updatePw(Long memberId, MemberRequestDTO.UpdatePw request);

        void resetPw(MemberRequestDTO.ResetPw request);

        boolean existsByStudentId(String studentId);
    }

    interface AdminService {
        MemberResponseDTO.MemberInfo updateRole(Long currentMemberId, MemberRequestDTO.UpdateRole request);

        void deactivateMember(Long memberId);
    }
}
