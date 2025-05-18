package SMU.BAMBOO.Hompage.domain.libraryPostComment.repository;

import SMU.BAMBOO.Hompage.domain.libraryPostComment.entity.LibraryPostComment;

public interface LibraryPostCommentRepository {
    LibraryPostComment findByIdOrThrow(Long id);
    LibraryPostComment findParentByIdOrThrow(Long id);
    void validateBelongsToPost(LibraryPostComment comment, Long postId);
    int countByPostId(Long libraryPostId);
    void deleteAllByLibraryPostId(Long libraryPostId);
}