package SMU.BAMBOO.Hompage.domain.tag.repository;

import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);
    List<Tag> findByNameIn(List<String> names);
}
