package SMU.BAMBOO.Hompage.domain.libraryPostComment.repository;

import SMU.BAMBOO.Hompage.domain.libraryPostComment.entity.LibraryPostComment;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static SMU.BAMBOO.Hompage.domain.libraryPostComment.entity.QLibraryPostComment.libraryPostComment;

@Repository
@RequiredArgsConstructor
public class LibraryPostCommentRepositoryImpl implements LibraryPostCommentRepository {

    private final LibraryPostCommentJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

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
        if (!comment.getLibraryPost().getLibraryPostId().equals(postId)) {
            throw new CustomException(ErrorCode.COMMENT_LIBRARY_MISMATCH);
        }
    }

    @Override
    public int countByPostId(Long libraryPostId) {
        return jpaRepository.countByLibraryPost_LibraryPostId(libraryPostId);
    }

    @Override
    public void deleteAllByLibraryPostId(Long libraryPostId) {
        queryFactory.delete(libraryPostComment)
                .where(libraryPostComment.libraryPost.libraryPostId.eq(libraryPostId))
                .execute();
    }
}