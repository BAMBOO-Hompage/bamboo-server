package SMU.BAMBOO.Hompage.domain.knowledgeComment.repository;

import SMU.BAMBOO.Hompage.domain.knowledgeComment.entity.KnowledgeComment;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KnowledgeCommentRepositoryImpl implements KnowledgeCommentRepository {

    private final KnowledgeCommentJpaRepository knowledgeCommentJpaRepository;

    @Override
    public KnowledgeComment findByIdOrThrow(Long id) {
        return knowledgeCommentJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.KNOWLEDGE_COMMENT_NOT_EXIST));
    }

    @Override
    public KnowledgeComment findParentByIdOrThrow(Long id) {
        return knowledgeCommentJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.KNOWLEDGE_PARENT_COMMENT_NOT_EXIST));
    }

    @Override
    public void validateBelongsToKnowledge(KnowledgeComment comment, Long knowledgeId) {
        if (!comment.getKnowledge().getKnowledgeId().equals(knowledgeId)) {
            throw new CustomException(ErrorCode.COMMENT_KNOWLEDGE_MISMATCH);
        }
    }
}