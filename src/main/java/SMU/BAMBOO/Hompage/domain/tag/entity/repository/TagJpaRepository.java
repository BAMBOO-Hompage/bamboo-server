package SMU.BAMBOO.Hompage.domain.tag.entity.repository;

import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);
}
