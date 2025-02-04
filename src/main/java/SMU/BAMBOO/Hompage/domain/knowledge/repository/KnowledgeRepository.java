package SMU.BAMBOO.Hompage.domain.knowledge.repository;

import SMU.BAMBOO.Hompage.domain.enums.KnowledgeType;
import SMU.BAMBOO.Hompage.domain.knowledge.entity.Knowledge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface KnowledgeRepository {
    Optional<Knowledge> findById(Long id);
    Knowledge save(Knowledge knowledge);
    void deleteById(Long id);
    Page<Knowledge> findAllByType(KnowledgeType type, Pageable pageable);
    Page<Knowledge> findAllByTitle(String title, Pageable pageable);
    Page<Knowledge> findByTypeAndTitle(KnowledgeType type, String keyword, Pageable pageable);
    Page<Knowledge> findAll(Pageable pageable);
}
