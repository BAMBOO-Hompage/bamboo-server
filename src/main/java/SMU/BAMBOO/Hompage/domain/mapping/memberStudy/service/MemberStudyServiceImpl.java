package SMU.BAMBOO.Hompage.domain.mapping.memberStudy.service;

import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.dto.MemberStudyResponseDTO;
import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.entity.MemberStudy;
import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.repository.MemberStudyRepository;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.domain.study.repository.StudyRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberStudyServiceImpl implements MemberStudyService {

    private final MemberStudyRepository memberStudyRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    /**
     * 출석 처리 (oCount 증가)
     */
    @Override
    @Transactional
    public MemberStudyResponseDTO.Detail attendance(Long memberId, Long studyId) {
        validateMemberAndStudyExists(memberId, studyId);
        MemberStudy memberStudy = findMemberStudy(memberId, studyId);
        memberStudy.increaseOCount();

        return MemberStudyResponseDTO.Detail.from(memberStudy);
    }

    /**
     * 결석 처리 (xCount 증가)
     */
    @Override
    @Transactional
    public MemberStudyResponseDTO.Detail absence(Long memberId, Long studyId) {
        validateMemberAndStudyExists(memberId, studyId);
        MemberStudy memberStudy = findMemberStudy(memberId, studyId);
        memberStudy.increaseXCount();

        return MemberStudyResponseDTO.Detail.from(memberStudy);
    }

    /**
     * memberId와 studyId를 기반으로 MemberStudy 조회
     */
    private MemberStudy findMemberStudy(Long memberId, Long studyId) {
        return memberStudyRepository.findByMember_MemberIdAndStudy_StudyId(memberId, studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
    }

    /**
     * 회원, 스터디가 실제 존재하는지 검증
     */
    private void validateMemberAndStudyExists(Long memberId, Long studyId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_EXIST));
    }
}
