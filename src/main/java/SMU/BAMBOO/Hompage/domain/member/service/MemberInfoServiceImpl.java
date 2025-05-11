package SMU.BAMBOO.Hompage.domain.member.service;

import SMU.BAMBOO.Hompage.domain.member.dto.MemberRequestDTO;
import SMU.BAMBOO.Hompage.domain.member.dto.MemberResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberInfoServiceImpl implements MemberService.MemberInfoService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AwsS3Facade awsS3Facade;

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
    public Page<MemberResponseDTO.MemberInfo> getMembers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("role"));
        return memberRepository.findAllSortByRole(pageable)
                .map(MemberResponseDTO.MemberInfo::from);
    }

    /**
     * 프로필 수정 (전화번호, 이미지)
     */
    @Transactional
    @Override
    public MemberResponseDTO.MyPage updateProfile(Long memberId, MemberRequestDTO.UpdateProfile request) {
        Member member = getMemberById(memberId);

        String profileImageUrl = null;
        if (request.profileImage() != null && !request.profileImage().isEmpty()) {
            // 기존 프로필 이미지 삭제
            String oldImageUrl = member.getProfileImageUrl();
            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                awsS3Facade.deleteFile(oldImageUrl);
            }

            // 새 프로필 이미지 업로드
            MultipartFile file = request.profileImage();
            profileImageUrl = awsS3Facade.uploadFile("profile-images", file, true);
        } else {
            profileImageUrl = member.getProfileImageUrl();
        }

        member.updateProfile(request.phoneNumber(), profileImageUrl);

        return MemberResponseDTO.MyPage.from(member);
    }

    /**
     * 프로필 이미지 삭제
     */
    @Transactional
    @Override
    public MemberResponseDTO.MyPage deleteProfileImage(Long memberId) {
        Member member = getMemberById(memberId);

        String oldImageUrl = member.getProfileImageUrl();
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            awsS3Facade.deleteFile(oldImageUrl);
        }

        member.setBasicProfileImage();

        return MemberResponseDTO.MyPage.from(member);
    }

    /**
     * 비밀번호 수정
     */
    @Transactional
    @Override
    public void updatePw(Long memberId, MemberRequestDTO.UpdatePw request) {
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
     * 비밀번호 초기화
     */
    @Transactional
    @Override
    public void resetPw(MemberRequestDTO.ResetPw request) {
        if (!request.newPassword1().equals(request.newPassword2())) {
            throw new CustomException(ErrorCode.USER_PASSWORD_MISMATCH);
        }

        Member member = memberRepository.getByStudentId(request.studentId());

        String newPassword = passwordEncoder.encode(request.newPassword1());
        member.updatePw(newPassword);
    }

    /**
     * 학번으로 회원 존재 여부 검증
     */
    @Override
    public boolean existsByStudentId(String studentId) {
        return memberRepository.existsByStudentId(studentId);
    }

    // ID로 회원 반환
    private Member getMemberById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
    }
}
