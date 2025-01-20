package SMU.BAMBOO.Hompage.domain.libraryPost.repository;

import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;

import java.util.List;
import java.util.Optional;

public interface LibraryPostRepository {

    Optional<LibraryPost> findById(Long id);

    Optional<LibraryPost> findByName(String name);

    LibraryPost save(LibraryPost libraryPost);

    List<LibraryPost> findAll();
}
