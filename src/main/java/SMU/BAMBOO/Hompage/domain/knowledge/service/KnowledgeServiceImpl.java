package SMU.BAMBOO.Hompage.domain.knowledge.service;

import SMU.BAMBOO.Hompage.domain.enums.KnowledgeType;
import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeRequestDTO;
import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeResponseDTO;
import SMU.BAMBOO.Hompage.domain.knowledge.entity.Knowledge;
import SMU.BAMBOO.Hompage.domain.knowledge.repository.KnowledgeRepository;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.AwsS3Service;
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
    private final AwsS3Service awsS3Service;

    private Knowledge getKnowledgeById(Long id) {
        return knowledgeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.KNOWLEDGE_NOT_EXIST));
    }

    @Override
    @Transactional
    public KnowledgeResponseDTO.Create create(KnowledgeRequestDTO.Create request, Member member, List<MultipartFile> images, List<MultipartFile> files) {
        // 파일 업로드 처리
        List<String> imageUrls = (images != null && !images.isEmpty()) ? awsS3Service.uploadFiles("knowledge/images", images) : new ArrayList<>();
        List<String> fileUrls = (files != null && !files.isEmpty()) ? awsS3Service.uploadFiles("knowledge/files", files) : new ArrayList<>();

        Knowledge knowledge = Knowledge.from(request, member, imageUrls, fileUrls);
        Knowledge saveKnowledge = knowledgeRepository.save(knowledge);

        return KnowledgeResponseDTO.Create.from(saveKnowledge);
    }

    @Override
    public KnowledgeResponseDTO.GetOne getById(Long id) {
        Knowledge knowledge = getKnowledgeById(id);
        return KnowledgeResponseDTO.GetOne.from(knowledge);
    }

    @Override
    public Page<KnowledgeResponseDTO.GetOne> getKnowledges(String type, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // type 유효성 검사
        KnowledgeType knowledgeType = null;
        if (type != null && !type.isEmpty()) {
            try {
                knowledgeType = KnowledgeType.valueOf(type.toUpperCase());
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

    @Override
    @Transactional
    public KnowledgeResponseDTO.Update update(Long id, KnowledgeRequestDTO.Update request, List<Object> images, List<Object> files) {
        Knowledge knowledge = getKnowledgeById(id);

        List<String> finalImageUrls = new ArrayList<>();
        List<String> finalFileUrls = new ArrayList<>();

        for (Object image : images) {
            if (image instanceof String url) { // 기존 이미지 URL이면 그대로 사용
                finalImageUrls.add(url);
            } else if (image instanceof MultipartFile file) { // 새 이미지 파일이면 업로드 후 URL 저장
                String uploadedUrl = awsS3Service.uploadFile("knowledge/images", file);
                finalImageUrls.add(uploadedUrl);
            }
        }

        for (Object file : files) {
            if (file instanceof String url) { // 기존 파일 URL이면 그대로 유지
                finalFileUrls.add(url);
            } else if (file instanceof MultipartFile multipartFile) { // 새 파일이면 업로드 후 URL 저장
                String uploadedUrl = awsS3Service.uploadFile("knowledge/files", multipartFile);
                finalFileUrls.add(uploadedUrl);
            }
        }

        knowledge.update(request, finalImageUrls, finalFileUrls);

        return KnowledgeResponseDTO.Update.from(knowledge);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Knowledge knowledge = getKnowledgeById(id);

        List<String> imageUrls = knowledge.getImages();
        List<String> fileUrls = knowledge.getImages();

        imageUrls.forEach(awsS3Service::deleteFile);
        fileUrls.forEach(awsS3Service::deleteFile);

        knowledgeRepository.deleteById(id);
    }
}
