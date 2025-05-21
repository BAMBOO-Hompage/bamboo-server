package SMU.BAMBOO.Hompage.domain.knowledgeComment.repository;

import SMU.BAMBOO.Hompage.domain.knowledgeComment.entity.KnowledgeComment;

public interface KnowledgeCommentRepository {
    KnowledgeComment findByIdOrThrow(Long id);
    KnowledgeComment findParentByIdOrThrow(Long id);
    void validateBelongsToKnowledge(KnowledgeComment comment, Long knowledgeId);
    void deleteAllByKnowledgeId(Long knowledgeId);
}

