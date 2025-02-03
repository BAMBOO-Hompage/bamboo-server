package SMU.BAMBOO.Hompage.domain.tag.repository;

import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    @Override
    public Optional<Tag> findById(Long id) {
        return tagJpaRepository.findById(id);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return tagJpaRepository.findByName(name);
    }

    @Override
    public List<Tag> findAll() {
        return tagJpaRepository.findAll();
    }

    @Override
    public List<Tag> findByNameIn(List<String> names) {
        return tagJpaRepository.findByNameIn(names);
    }

    @Override
    public Tag save(Tag tag) {
        return tagJpaRepository.save(tag);
    }

    @Override
    public void saveAll(List<Tag> tags) {
        tagJpaRepository.saveAll(tags);
    }

    @Override
    public void deleteById(Long id) {
        tagJpaRepository.deleteById(id);
    }

}
