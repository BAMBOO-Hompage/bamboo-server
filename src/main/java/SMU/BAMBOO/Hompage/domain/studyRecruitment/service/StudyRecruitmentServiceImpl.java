package SMU.BAMBOO.Hompage.domain.studyRecruitment.service;

import SMU.BAMBOO.Hompage.domain.studyRecruitment.dto.StudyRecruitmentRequestDTO;
import SMU.BAMBOO.Hompage.domain.studyRecruitment.dto.StudyRecruitmentResponseDTO;
import SMU.BAMBOO.Hompage.domain.studyRecruitment.entity.StudyRecruitment;
import SMU.BAMBOO.Hompage.domain.studyRecruitment.repository.StudyRecruitmentRepository;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyRecruitmentServiceImpl implements StudyRecruitmentService {

    private final StudyRecruitmentRepository studyRecruitmentRepository;

    @Override
    @Transactional
    public StudyRecruitmentResponseDTO.Create create(StudyRecruitmentRequestDTO.Create request, Member member) {
        StudyRecruitment studyRecruitment = StudyRecruitment.builder()
                .writerId(member.getMemberId())
                .writerName(member.getName())
                .title(request.title())
                .content(request.content())
                .maxMembers(request.maxMembers())
                .deadline(request.deadline())
                .build();
        StudyRecruitment saved = studyRecruitmentRepository.save(studyRecruitment);
        return StudyRecruitmentResponseDTO.Create.from(saved);
    }

    @Override
    public StudyRecruitmentResponseDTO.GetOne getById(Long id) {
        StudyRecruitment sr = studyRecruitmentRepository.getById(id);
        return StudyRecruitmentResponseDTO.GetOne.from(sr);
    }

    @Override
    public Page<StudyRecruitmentResponseDTO.GetOne> getStudyRecruitments(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StudyRecruitment> srPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            srPage = studyRecruitmentRepository.findAllByTitle(keyword, pageable);
        } else {
            srPage = studyRecruitmentRepository.findAll(pageable);
        }
        return srPage.map(StudyRecruitmentResponseDTO.GetOne::from);
    }

    @Override
    @Transactional
    public StudyRecruitmentResponseDTO.Update update(Member member, Long id, StudyRecruitmentRequestDTO.Update request) {
        StudyRecruitment sr = studyRecruitmentRepository.getById(id);
        validateOwnerOrAdmin(member, sr, ErrorCode.UNAUTHORIZED_UPDATE);
        sr.update(request);
        return StudyRecruitmentResponseDTO.Update.from(sr);
    }

    @Override
    @Transactional
    public void delete(Member member, Long id) {
        StudyRecruitment sr = studyRecruitmentRepository.getById(id);
        validateOwnerOrAdmin(member, sr, ErrorCode.UNAUTHORIZED_DELETE);
        studyRecruitmentRepository.deleteById(id);
    }

    private void validateOwnerOrAdmin(Member member, StudyRecruitment sr, ErrorCode errorCode) {
        boolean isAdmin = member.getRole().name().equals("ROLE_ADMIN") || member.getRole().name().equals("ROLE_OPS");
        boolean isOwner = sr.getWriterId().equals(member.getMemberId());
        if (!isOwner && !isAdmin) {
            throw new CustomException(errorCode);
        }
    }
}
