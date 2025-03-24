package SMU.BAMBOO.Hompage.domain.knowledgeComment.repository;

import SMU.BAMBOO.Hompage.domain.knowledgeComment.entity.KnowledgeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KnowledgeCommentJpaRepository extends JpaRepository<KnowledgeComment, Long> {
    Page<KnowledgeComment> findAllByKnowledge_KnowledgeIdAndParentIsNullOrderByCreatedAtAsc(Long knowledgeId, Pageable pageable);
}
