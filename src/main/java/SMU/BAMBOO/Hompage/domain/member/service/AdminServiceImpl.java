package SMU.BAMBOO.Hompage.domain.member.service;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.member.dto.MemberRequestDTO;
import SMU.BAMBOO.Hompage.domain.member.dto.MemberResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements MemberService.AdminService {

    private final MemberRepository memberRepository;

    /**
     * 권한 변경
     */
    @Transactional
    @Override
    public MemberResponseDTO.MemberInfo updateRole(Long currentMemberId, MemberRequestDTO.UpdateRole request) {
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

        return MemberResponseDTO.MemberInfo.from(member);
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

    // ID로 회원 반환
    private Member getMemberById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
    }
}
