package SMU.BAMBOO.Hompage.domain.knowledge.repository;

import SMU.BAMBOO.Hompage.domain.knowledge.entity.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeJpaRepository extends JpaRepository<Knowledge, Long> {
}
