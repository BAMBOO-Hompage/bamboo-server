package SMU.BAMBOO.Hompage.domain.knowledgeComment.service;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.knowledge.entity.Knowledge;
import SMU.BAMBOO.Hompage.domain.knowledge.repository.KnowledgeRepository;
import SMU.BAMBOO.Hompage.domain.knowledgeComment.dto.KnowledgeCommentRequestDTO;
import SMU.BAMBOO.Hompage.domain.knowledgeComment.dto.KnowledgeCommentResponseDTO;
import SMU.BAMBOO.Hompage.domain.knowledgeComment.entity.KnowledgeComment;
import SMU.BAMBOO.Hompage.domain.knowledgeComment.repository.KnowledgeCommentJpaRepository;
import SMU.BAMBOO.Hompage.domain.knowledgeComment.repository.KnowledgeCommentRepository;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KnowledgeCommentServiceImpl implements KnowledgeCommentService {

    private final KnowledgeRepository knowledgeRepository;
    private final KnowledgeCommentJpaRepository knowledgeCommentJpaRepository;
    private final KnowledgeCommentRepository knowledgeCommentRepository;

    /** 댓글 생성 */
    @Override
    @Transactional
    public KnowledgeCommentResponseDTO.Create createComment(Long knowledgeId, KnowledgeCommentRequestDTO.Create request, Member member) {
        Knowledge knowledge = getKnowledgeById(knowledgeId);
        KnowledgeComment parent = (request.getParentId() != null)
                ? knowledgeCommentRepository.findParentByIdOrThrow(request.getParentId())
                : null;
        KnowledgeComment comment = KnowledgeComment.from(request.getContent(), knowledge, member, parent);

        return KnowledgeCommentResponseDTO.Create.from(knowledgeCommentJpaRepository.save(comment));
    }

    /** 댓글 목록 조회 */
    @Override
    public Page<KnowledgeCommentResponseDTO.GetOne> getCommentsByKnowledgeId(Long knowledgeId, Pageable pageable) {
        getKnowledgeById(knowledgeId); // 존재 검증
        return knowledgeCommentJpaRepository
                .findAllByKnowledge_KnowledgeIdAndParentIsNullOrderByCreatedAtAsc(knowledgeId, pageable)
                .map(KnowledgeCommentResponseDTO.GetOne::from);
    }

    /** 댓글 수정 */
    @Override
    @Transactional
    public List<KnowledgeCommentResponseDTO.GetOne> updateComment(Long knowledgeId, Long commentId, KnowledgeCommentRequestDTO.Update request, Member member) {
        KnowledgeComment comment = knowledgeCommentRepository.findByIdOrThrow(commentId);
        knowledgeCommentRepository.validateBelongsToKnowledge(comment, knowledgeId);
        validateCommentAuthority(comment, member);
        comment.updateContent(request.getContent());
        return List.of(KnowledgeCommentResponseDTO.GetOne.from(comment));
    }

    /** 댓글 삭제 */
    @Override
    @Transactional
    public void deleteComment(Long knowledgeId, Long commentId, Member member) {
        KnowledgeComment comment = knowledgeCommentRepository.findByIdOrThrow(commentId);
        knowledgeCommentRepository.validateBelongsToKnowledge(comment, knowledgeId);
        validateCommentAuthority(comment, member);
        if (!comment.getChildren().isEmpty()) {
            comment.softDelete();
        } else {
            knowledgeCommentJpaRepository.delete(comment);
        }
    }

    private Knowledge getKnowledgeById(Long knowledgeId) {
        return knowledgeRepository.findById(knowledgeId)
                .orElseThrow(() -> new CustomException(ErrorCode.KNOWLEDGE_NOT_EXIST));
    }

    private void validateCommentAuthority(KnowledgeComment comment, Member member) {
        boolean isOwner = comment.getWriterStudentId().equals(member.getStudentId());
        boolean isAdminOrOps = member.getRole() == Role.ROLE_ADMIN || member.getRole() == Role.ROLE_OPS;

        if (!(isOwner || isAdminOrOps)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_DELETE);
        }
    }
}
