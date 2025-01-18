package SMU.BAMBOO.Hompage.domain.member.service;

import SMU.BAMBOO.Hompage.domain.member.dto.request.MemberLoginDto;
import SMU.BAMBOO.Hompage.domain.member.dto.request.MemberSignUpDto;
import SMU.BAMBOO.Hompage.domain.member.dto.response.LoginResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.jwt.userDetails.CustomUserDetails;
import SMU.BAMBOO.Hompage.global.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    // 학번으로 중복 회원 검증
    private void validateDuplicateMember(String studentId) {
        if (memberRepository.findByStudentId(studentId).isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }
    }

    // 메일로 중복 회원 검증
    private void validateDuplicateMemberByMail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }
    }

    // 학번으로 회원 존재 여부 검증 - 존재하면 반환
    private Member validateExistMember(String studentId){
        return memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
    }

    public MemberResponse signUp(MemberSignUpDto request, BCryptPasswordEncoder encoder) {
        validateDuplicateMember(request.studentId());
        Member member = memberRepository.save(Member.from(request, encoder));
        return MemberResponse.from(member);
    }

    public LoginResponse login(MemberLoginDto request, HttpServletResponse response) {
        Member member = validateExistMember(request.studentId());

        if (!passwordEncoder.matches(request.password(), member.getPw())) {
            throw new CustomException(ErrorCode.USER_WRONG_PASSWORD);
        }

        String accessToken = jwtUtil.createAccessToken(request.studentId(), member.getRole().name());
        String refreshToken = jwtUtil.createRefreshToken(new CustomUserDetails(request.studentId(), null, member.getRole()));

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh-Token", refreshToken);

        return LoginResponse.from(member);
    }

    public Member getMember(String studentId) {
        return memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
    }
}
