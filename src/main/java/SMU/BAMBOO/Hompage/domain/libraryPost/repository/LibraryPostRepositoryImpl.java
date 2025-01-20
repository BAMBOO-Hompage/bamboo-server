package SMU.BAMBOO.Hompage.domain.libraryPost.repository;

import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LibraryPostRepositoryImpl implements LibraryPostRepository {

    private final LibraryPostJpaRepository libraryPostJpaRepository;

    @Override
    public Optional<LibraryPost> findById(Long id) {
        return libraryPostJpaRepository.findById(id);
    }

    @Override
    public Optional<LibraryPost> findByPaperName(String name) {
        return libraryPostJpaRepository.findByPaperName(name);
    }

    @Override
    public LibraryPost save(LibraryPost libraryPost) {
        return libraryPostJpaRepository.save(libraryPost);
    }

    @Override
    public void deleteById(Long id) {
        libraryPostJpaRepository.deleteById(id);
    }

    @Override
    public List<LibraryPost> findAll() {
        return libraryPostJpaRepository.findAll();
    }
}
