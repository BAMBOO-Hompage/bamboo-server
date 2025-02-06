package SMU.BAMBOO.Hompage.domain.member.service;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.member.dto.request.*;
import SMU.BAMBOO.Hompage.domain.member.dto.response.LoginResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MyPageResponse;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.jwt.userDetails.CustomUserDetails;
import SMU.BAMBOO.Hompage.global.jwt.util.JwtUtil;
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

    /**
     * 회원가입
     */
    @Transactional
    @Override
    public MemberResponse signUp(MemberSignUpDto request, BCryptPasswordEncoder encoder) {
        validateDuplicateMember(request.studentId());
        Member member = memberRepository.save(Member.from(request, encoder));
        return MemberResponse.from(member);
    }

    /**
     * 로그인
     */
    @Transactional
    @Override
    public LoginResponse login(MemberLoginDto request, HttpServletResponse response) {
        Member member = getMemberByStudentId(request.studentId());

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
    @Override
    public Member getMember(String studentId) {
        return memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
    }

    /**
     * 회원 정보 목록
     */
    @Override
    public Page<MemberResponse> getMembers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("role"));
        return memberRepository.findAllSortByRole(pageable)
                .map(MemberResponse::from);
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

    /**
     * 프로필 수정 (전화번호, 이미지)
     */
    @Transactional
    @Override
    public MyPageResponse updateProfile(Long memberId, UpdateProfileDto request) {
        Member member = getMemberById(memberId);

        String profileImageUrl = null;
        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            // 기존 프로필 이미지 삭제
            String oldImageUrl = member.getProfileImageUrl();
            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                String key = awsS3Service.extractS3Key(oldImageUrl);
                awsS3Service.deleteFile(key);
            }

            // 새 프로필 이미지 업로드
            MultipartFile file = request.getProfileImage();
            profileImageUrl = awsS3Service.uploadFile("profile-images", file, true);
        } else {
            profileImageUrl = member.getProfileImageUrl();
        }

        member.updateProfile(request.getPhoneNumber(), profileImageUrl);

        return MyPageResponse.from(member);
    }

    /**
     * 프로필 이미지 삭제
     */
    @Transactional
    @Override
    public MyPageResponse deleteProfileImage(Long memberId) {
        Member member = getMemberById(memberId);

        String oldImageUrl = member.getProfileImageUrl();
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            String key = awsS3Service.extractS3Key(oldImageUrl);
            awsS3Service.deleteFile(key);
        }

        member.setBasicProfileImage();

        return MyPageResponse.from(member);
    }

    /**
     * 비밀번호 수정
     */
    @Transactional
    @Override
    public void updatePw(Long memberId, UpdatePwDto request) {
        Member member = getMemberById(memberId);

        // 회원의 비밀번호와 요청의 비밀번호를 비교
        if (!passwordEncoder.matches(request.password(), member.getPw())) {
            throw new CustomException(ErrorCode.USER_WRONG_PASSWORD);
        }

        // 동일한 비밀번호로 변경 불가
        if (passwordEncoder.matches(request.newPassword(), member.getPw())) {
            throw new CustomException(ErrorCode.USER_SAME_PASSWORD);
        }

        String newPassword = passwordEncoder.encode(request.newPassword());
        member.updatePw(newPassword);
    }

    /**
     * 권한 변경
     */
    @Transactional
    @Override
    public MemberResponse updateRole(Long currentMemberId, UpdateRoleDto request) {
        Member currentMember = getMemberById(currentMemberId);

        // 임원진 권한 확인
        if (!currentMember.getRole().equals(Role.ROLE_OPS)) {
            throw new CustomException(ErrorCode.USER_FORBIDDEN);
        }

        // 변경 대상 회원 조회
        Member member = getMemberById(request.memberId());

        // 권한 변경
        try {
            Role role = Role.valueOf(request.role());
            member.updateRole(role);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ROLE);
        }

        return MemberResponse.from(member);
    }

    /**
     * 임시 - 임원 권한이 필요 없는 권한 변경
     */
    @Transactional
    @Override
    public MemberResponse testUpdateRole(TestUpdateRoleDto request) {
        // 변경 대상 회원 조회
        Member member = getMemberById(request.memberId());

        // 권한 변경
        try {
            Role role = Role.valueOf(request.role());
            member.updateRole(role);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ROLE);
        }

        return MemberResponse.from(member);
    }

    /**
     * 회원 비활성화 (Soft Delete)
     */
    @Transactional
    @Override
    public void deactivateMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        member.deactivate();
    }

    /**
     * 7일이 지난 Soft Deleted 데이터 완전 삭제
     */
    @Scheduled(cron = "0 0 4 * * ?") // (초 분 시 일 월 요일) -> 매일 새벽 4시 실행
    @Transactional
    public void removeOldDeletedMembers() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7);
        memberRepository.hardDeleteOldMembers(threshold);
    }
}
