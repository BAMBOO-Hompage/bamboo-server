package SMU.BAMBOO.Hompage.domain.libraryPostComment.repository;

import SMU.BAMBOO.Hompage.domain.libraryPostComment.entity.LibraryPostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryPostCommentJpaRepository extends JpaRepository<LibraryPostComment, Long> {
    Page<LibraryPostComment> findAllByLibraryPost_LibraryPostIdAndParentIsNullOrderByCreatedAtAsc(Long libraryPostId, Pageable pageable);
    int countByLibraryPost_LibraryPostId(Long libraryPostId);
}
