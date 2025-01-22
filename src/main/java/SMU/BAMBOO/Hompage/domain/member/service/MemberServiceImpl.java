package SMU.BAMBOO.Hompage.domain.member.service;

import SMU.BAMBOO.Hompage.domain.member.dto.request.MemberLoginDto;
import SMU.BAMBOO.Hompage.domain.member.dto.request.MemberSignUpDto;
import SMU.BAMBOO.Hompage.domain.member.dto.request.UpdateProfileDto;
import SMU.BAMBOO.Hompage.domain.member.dto.request.UpdatePwDto;
import SMU.BAMBOO.Hompage.domain.member.dto.response.LoginResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MyPageResponse;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.jwt.userDetails.CustomUserDetails;
import SMU.BAMBOO.Hompage.global.jwt.util.JwtUtil;
import SMU.BAMBOO.Hompage.global.upload.AwsS3Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final AwsS3Service awsS3Service;

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

    /**
     * 회원가입
     */
    @Transactional
    public MemberResponse signUp(MemberSignUpDto request, BCryptPasswordEncoder encoder) {
        validateDuplicateMember(request.studentId());
        Member member = memberRepository.save(Member.from(request, encoder));
        return MemberResponse.from(member);
    }

    /**
     * 로그인
     */
    @Transactional
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

    /**
     * 회원 정보
     */
    public Member getMember(String studentId) {
        return memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
    }

    /**
     * 로그아웃
     */
    @Transactional
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

    /**
     * 프로필 수정 (전화번호, 이미지)
     */
    @Transactional
    public MyPageResponse updateProfile(Long memberId, UpdateProfileDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        // 기존 프로필 이미지 삭제
        String oldImageUrl = member.getProfileImageUrl();
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            String key = awsS3Service.extractS3Key(oldImageUrl);
            awsS3Service.deleteFile(key);
        }

        // 새 프로필 이미지 업로드
        String profileImageUrl = null;
        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            MultipartFile file = request.getProfileImage();
            profileImageUrl = awsS3Service.uploadFile("profile-images", file);
        }

        member.updateProfile(request.getPhoneNumber(), profileImageUrl);

        return MyPageResponse.from(member);
    }

    /**
     * 비밀번호 수정
     */
    @Transactional
    public void updatePw(Long memberId, UpdatePwDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        // 회원의 비밀번호와 요청의 비밀번호를 비교
        if (!passwordEncoder.matches(request.password(), member.getPw())) {
            throw new CustomException(ErrorCode.USER_WRONG_PASSWORD);
        }

        String newPassword = passwordEncoder.encode(request.newPassword());
        member.updatePw(newPassword);
    }
}
