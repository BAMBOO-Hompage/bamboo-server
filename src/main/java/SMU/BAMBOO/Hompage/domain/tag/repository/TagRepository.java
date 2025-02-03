package SMU.BAMBOO.Hompage.domain.tag.repository;

import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Optional<Tag> findById(Long id);

    Optional<Tag> findByName(String name);

    List<Tag> findAll();

    List<Tag> findByNameIn(List<String> names);

    Tag save(Tag tag);

    void saveAll(List<Tag> tags);

    void deleteById(Long id);

}
