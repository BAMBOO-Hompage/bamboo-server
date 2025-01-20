package SMU.BAMBOO.Hompage.domain.libraryPost.repository;

import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryPostJpaRepository extends JpaRepository<LibraryPost, Long> {

    Optional<LibraryPost> findByPaperName(String paperName);
}
