package SMU.BAMBOO.Hompage.domain.libraryPost.repository;

import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LibraryPostRepository {

    Optional<LibraryPost> findById(Long id);
    Optional<LibraryPost> findByPaperName(String paperName);
    LibraryPost save(LibraryPost libraryPost);
    Page<LibraryPost> findByPage(Pageable pageable);
    Page<LibraryPost> findByPaperName(String paperName, Pageable pageable);
    Page<LibraryPost> findByYear(String year, Pageable pageable);
    Page<LibraryPost> findByTag(String tag, Pageable pageable);
    void deleteById(Long id);
}
