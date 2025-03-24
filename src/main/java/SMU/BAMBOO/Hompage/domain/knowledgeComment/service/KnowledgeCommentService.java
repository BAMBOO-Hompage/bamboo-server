package SMU.BAMBOO.Hompage.domain.knowledgeComment.service;

import SMU.BAMBOO.Hompage.domain.knowledgeComment.dto.KnowledgeCommentRequestDTO;
import SMU.BAMBOO.Hompage.domain.knowledgeComment.dto.KnowledgeCommentResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KnowledgeCommentService {
    KnowledgeCommentResponseDTO.Create createComment(Long knowledgeId, KnowledgeCommentRequestDTO.Create request, Member member);
    Page<KnowledgeCommentResponseDTO.GetOne> getCommentsByKnowledgeId(Long knowledgeId, Pageable pageable);
    List<KnowledgeCommentResponseDTO.GetOne> updateComment(Long knowledgeId, Long commentId, KnowledgeCommentRequestDTO.Update request, Member member);
    void deleteComment(Long knowledgeId, Long commentId, Member member);
}
