package SMU.BAMBOO.Hompage.domain.knowledge.service;

import SMU.BAMBOO.Hompage.domain.enums.KnowledgeType;
import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeRequestDTO;
import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeResponseDTO;
import SMU.BAMBOO.Hompage.domain.knowledge.entity.Knowledge;
import SMU.BAMBOO.Hompage.domain.knowledge.repository.KnowledgeRepository;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Facade;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Builder
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KnowledgeServiceImpl implements KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;
    private final AwsS3Facade awsS3Facade;

    private Knowledge getKnowledgeById(Long id) {
        return knowledgeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.KNOWLEDGE_NOT_EXIST));
    }

    /** 지식공유 글 생성 */
    @Override
    @Transactional
    public KnowledgeResponseDTO.Create create(KnowledgeRequestDTO.Create request, Member member, List<MultipartFile> images, List<MultipartFile> files) {
        // 파일 업로드 처리
        List<String> imageUrls = (images != null && !images.isEmpty())
                ? awsS3Facade.uploadFiles("knowledge/images", images, true)
                : new ArrayList<>();
        List<String> fileUrls = (files != null && !files.isEmpty())
                ? awsS3Facade.uploadFiles("knowledge/files", files, false)
                : new ArrayList<>();

        Knowledge knowledge = Knowledge.from(request, member, imageUrls, fileUrls);
        Knowledge saveKnowledge = knowledgeRepository.save(knowledge);

        return KnowledgeResponseDTO.Create.from(saveKnowledge);
    }

    /** 지식공유 글 ID 단일 조회 */
    @Override
    public KnowledgeResponseDTO.GetOne getById(Long id) {
        Knowledge knowledge = getKnowledgeById(id);
        return KnowledgeResponseDTO.GetOne.from(knowledge);
    }

    /** 지식공유 글 목록 조회 - 각 경우마다 다른 검색
     * 1. Type, Keyword(검색어) 둘 다 있는 경우
     * 2. Type 있는 경우
     * 3. Keyword 있는 경우
     * 4. 둘 다 없는 경우
     */
    @Override
    public Page<KnowledgeResponseDTO.GetOne> getKnowledges(String type, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // type 유효성 검사
        KnowledgeType knowledgeType = null;
        if (type != null) {
            try {
                knowledgeType = KnowledgeType.from(type.trim());
            } catch (IllegalArgumentException e) {
                throw new CustomException(ErrorCode.KNOWLEDGE_INVALID_TYPE);
            }
        }

        // 조건에 따라 검색 함수 호출
        Page<Knowledge> knowledgePage;
        if (knowledgeType != null && keyword != null && !keyword.isEmpty()) {
            // type + keyword 검색
            knowledgePage = knowledgeRepository.findByTypeAndTitle(knowledgeType, keyword, pageable);
        } else if (knowledgeType != null) {
            // type 검색
            knowledgePage = knowledgeRepository.findAllByType(knowledgeType, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            // title 에서 keyword 검색
            knowledgePage = knowledgeRepository.findAllByTitle(keyword, pageable);
        } else {
            // 전체 조회
            knowledgePage = knowledgeRepository.findAll(pageable);
        }

        return knowledgePage.map(KnowledgeResponseDTO.GetOne::from);
    }

    /** 지식공유 글 수정 */
    @Override
    @Transactional
    public KnowledgeResponseDTO.Update update(Member member, Long id, KnowledgeRequestDTO.Update request,
                                              List<String> imageUrls, List<MultipartFile> newImages,
                                              List<String> fileUrls, List<MultipartFile> newFiles) {
        Knowledge knowledge = getKnowledgeById(id);
        validateOwnerOrAdmin(member, knowledge, ErrorCode.UNAUTHORIZED_UPDATE);

        List<String> finalImageUrls = new ArrayList<>(imageUrls != null ? imageUrls : new ArrayList<>());
        List<String> finalFileUrls = new ArrayList<>(fileUrls != null ? fileUrls : new ArrayList<>());

        // 새 이미지 업로드
        if (newImages != null && !newImages.isEmpty()) {
            List<String> uploadedImageUrls = awsS3Facade.uploadFiles("knowledge/images", newImages, true);
            finalImageUrls.addAll(uploadedImageUrls);
        }

        // 새 파일 업로드
        if (newFiles != null && !newFiles.isEmpty()) {
            List<String> uploadedFileUrls = awsS3Facade.uploadFiles("knowledge/files", newFiles, false);
            finalFileUrls.addAll(uploadedFileUrls);
        }

        knowledge.update(request, finalImageUrls, finalFileUrls);

        return KnowledgeResponseDTO.Update.from(knowledge);
    }

    /** 지식공유 글 삭제 */
    @Override
    @Transactional
    public void delete(Member member, Long id) {
        Knowledge knowledge = getKnowledgeById(id);
        validateOwnerOrAdmin(member, knowledge, ErrorCode.UNAUTHORIZED_DELETE);

        List<String> imageUrls = knowledge.getImages();
        List<String> fileUrls = knowledge.getFiles();

        imageUrls.forEach(awsS3Facade::deleteFile);
        fileUrls.forEach(awsS3Facade::deleteFile);

        knowledgeRepository.deleteById(id);
    }

    /**
     * 현재 사용자가 게시글의 작성자이거나 관리자/운영진인지 검증하는 메서드
     */
    private void validateOwnerOrAdmin(Member member, Knowledge knowledge, ErrorCode errorCode) {
        String currentStudentId = member.getStudentId();
        String currentRole = member.getRole().name();

        boolean isAdmin = currentRole.equals("ROLE_ADMIN") || currentRole.equals("ROLE_OPS");
        boolean isOwner = knowledge.getMember().getStudentId().equals(currentStudentId);

        if (!isOwner && !isAdmin) {
            throw new CustomException(errorCode);
        }
    }
}
