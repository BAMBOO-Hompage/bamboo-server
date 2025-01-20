package SMU.BAMBOO.Hompage.domain.tag.entity.repository;

import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Optional<Tag> findById(Long id);

    Optional<Tag> findByName(String name);

    List<Tag> findAll();

    Tag save(Tag tag);

    void deleteById(Long id);

}
