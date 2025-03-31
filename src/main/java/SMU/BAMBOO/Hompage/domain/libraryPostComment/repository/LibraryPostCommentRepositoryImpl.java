package SMU.BAMBOO.Hompage.domain.libraryPostComment.repository;

import SMU.BAMBOO.Hompage.domain.libraryPostComment.entity.LibraryPostComment;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LibraryPostCommentRepositoryImpl implements LibraryPostCommentRepository {

    private final LibraryPostCommentJpaRepository jpaRepository;

    @Override
    public LibraryPostComment findByIdOrThrow(Long id) {
        return jpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.LIBRARY_COMMENT_NOT_EXIST));
    }

    @Override
    public LibraryPostComment findParentByIdOrThrow(Long id) {
        return jpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.LIBRARY_PARENT_COMMENT_NOT_EXIST));
    }

    @Override
    public void validateBelongsToPost(LibraryPostComment comment, Long postId) {
        if (!comment.getPost().getLibraryPostId().equals(postId)) {
            throw new CustomException(ErrorCode.COMMENT_LIBRARY_MISMATCH);
        }
    }
}