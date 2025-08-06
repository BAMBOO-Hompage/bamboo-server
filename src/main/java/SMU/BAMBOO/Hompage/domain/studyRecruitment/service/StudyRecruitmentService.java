package SMU.BAMBOO.Hompage.domain.studyRecruitment.service;

import SMU.BAMBOO.Hompage.domain.studyRecruitment.dto.StudyRecruitmentRequestDTO;
import SMU.BAMBOO.Hompage.domain.studyRecruitment.dto.StudyRecruitmentResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import org.springframework.data.domain.Page;

public interface StudyRecruitmentService {
    StudyRecruitmentResponseDTO.Create create(StudyRecruitmentRequestDTO.Create request, Member member);
    StudyRecruitmentResponseDTO.GetOne getById(Long id);
    Page<StudyRecruitmentResponseDTO.GetOne> getStudyRecruitments(String keyword, int page, int size);
    StudyRecruitmentResponseDTO.Update update(Member member, Long id, StudyRecruitmentRequestDTO.Update request);
    void delete(Member member, Long id);
}
