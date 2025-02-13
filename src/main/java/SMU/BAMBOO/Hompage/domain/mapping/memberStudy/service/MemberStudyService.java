package SMU.BAMBOO.Hompage.domain.mapping.memberStudy.service;

import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.dto.MemberStudyResponseDTO;

public interface MemberStudyService {

    MemberStudyResponseDTO.Detail attendance(Long memberId, Long studyId);
    MemberStudyResponseDTO.Detail absence(Long memberId, Long studyId);
}
