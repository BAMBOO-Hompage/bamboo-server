package SMU.BAMBOO.Hompage.domain.member.service;

import SMU.BAMBOO.Hompage.domain.member.dto.MemberRequestDTO;
import SMU.BAMBOO.Hompage.domain.member.dto.MemberResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.jwt.userDetails.CustomUserDetails;
import SMU.BAMBOO.Hompage.global.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements MemberService.AuthenticationService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 회원가입
     */
    @Transactional
    @Override
    public MemberResponseDTO.MemberInfo signUp(MemberRequestDTO.SignUp request, BCryptPasswordEncoder encoder) {
        validateDuplicateMember(request.studentId());
        Member member = memberRepository.save(Member.from(request, encoder));
        return MemberResponseDTO.MemberInfo.from(member);
    }

    /**
     * 로그인
     */
    @Transactional
    @Override
    public MemberResponseDTO.Login login(MemberRequestDTO.Login request, HttpServletResponse response) {
        Member member = getMemberByStudentId(request.studentId());

        if (!passwordEncoder.matches(request.password(), member.getPw())) {
            throw new CustomException(ErrorCode.USER_WRONG_PASSWORD);
        }

        String accessToken = jwtUtil.createAccessToken(request.studentId(), member.getRole().name());
        String refreshToken = jwtUtil.createRefreshToken(new CustomUserDetails(request.studentId(), null, member.getRole()));

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh-Token", refreshToken);

        return MemberResponseDTO.Login.from(member);
    }

    /**
     * 로그아웃
     */
    @Transactional
    @Override
    public String logout(String accessToken) {

        // Access Token 검증
        if (accessToken == null || !jwtUtil.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.ACCESS_TOKEN_INVALID);
        }

        // Access Token 에서 studentId 추출
        String studendId = jwtUtil.getStudentId(accessToken);

        // Redis 에서 Refresh Token 삭제
        String key = "refresh_token:" + studendId;
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }

        // Access Token 블랙리스트 처리
        long expiration = jwtUtil.getExpiration(accessToken);
        redisTemplate.opsForValue().set("blacklist:" + accessToken, "true", expiration, TimeUnit.MILLISECONDS);

        return "로그아웃 성공";
    }


    // 학번으로 중복 회원 검증
    private void validateDuplicateMember(String studentId) {
        if (memberRepository.findByStudentId(studentId).isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }
    }

    // ID로 회원 반환
    private Member getMemberById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
    }

    // 학번으로 회원 반환
    private Member getMemberByStudentId(String studentId){
        return memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
    }
}
