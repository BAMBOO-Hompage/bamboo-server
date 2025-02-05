package SMU.BAMBOO.Hompage.domain.knowledge.service;

import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeRequestDTO;
import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface KnowledgeService {

    KnowledgeResponseDTO.Create create(KnowledgeRequestDTO.Create request, Member member, List<MultipartFile> images, List<MultipartFile> files);
    KnowledgeResponseDTO.GetOne getById(Long id);
    Page<KnowledgeResponseDTO.GetOne> getKnowledges(String type, String keyword, int page, int size);
    KnowledgeResponseDTO.Update update(Long id, KnowledgeRequestDTO.Update request,
                                       List<String> imageUrls, List<MultipartFile> newImages,
                                       List<String> fileUrls, List<MultipartFile> newFiles);
    void delete(Long id);
}