package SMU.BAMBOO.Hompage.domain.libraryPostComment.service;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import SMU.BAMBOO.Hompage.domain.libraryPost.repository.LibraryPostRepository;
import SMU.BAMBOO.Hompage.domain.libraryPostComment.dto.LibraryPostCommentRequestDTO;
import SMU.BAMBOO.Hompage.domain.libraryPostComment.dto.LibraryPostCommentResponseDTO;
import SMU.BAMBOO.Hompage.domain.libraryPostComment.entity.LibraryPostComment;
import SMU.BAMBOO.Hompage.domain.libraryPostComment.repository.LibraryPostCommentJpaRepository;
import SMU.BAMBOO.Hompage.domain.libraryPostComment.repository.LibraryPostCommentRepository;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LibraryPostCommentServiceImpl implements LibraryPostCommentService {

    private final LibraryPostRepository libraryPostRepository;
    private final LibraryPostCommentJpaRepository libraryPostCommentJpaRepository;
    private final LibraryPostCommentRepository libraryPostCommentRepository;

    @Override
    @Transactional
    public LibraryPostCommentResponseDTO.Create createComment(Long postId, LibraryPostCommentRequestDTO.Create request, Member member) {
        LibraryPost post = getPostById(postId);
        LibraryPostComment parent = request.getParentId() != null
                ? libraryPostCommentRepository.findParentByIdOrThrow(request.getParentId())
                : null;
        LibraryPostComment comment = LibraryPostComment.from(request.getContent(), post, member, parent);
        return LibraryPostCommentResponseDTO.Create.from(libraryPostCommentJpaRepository.save(comment));
    }

    @Override
    public Page<LibraryPostCommentResponseDTO.GetOne> getCommentsByPostId(Long postId, Pageable pageable) {
        getPostById(postId);
        return libraryPostCommentJpaRepository.findAllByLibraryPost_LibraryPostIdAndParentIsNullOrderByCreatedAtAsc(postId, pageable)
                .map(LibraryPostCommentResponseDTO.GetOne::from);
    }

    @Override
    @Transactional
    public List<LibraryPostCommentResponseDTO.GetOne> updateComment(Long postId, Long commentId, LibraryPostCommentRequestDTO.Update request, Member member) {
        LibraryPostComment comment = libraryPostCommentRepository.findByIdOrThrow(commentId);
        libraryPostCommentRepository.validateBelongsToPost(comment, postId);
        validateAuthority(comment, member);
        comment.updateContent(request.getContent());
        return List.of(LibraryPostCommentResponseDTO.GetOne.from(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long postId, Long commentId, Member member) {
        LibraryPostComment comment = libraryPostCommentRepository.findByIdOrThrow(commentId);
        libraryPostCommentRepository.validateBelongsToPost(comment, postId);
        validateAuthority(comment, member);
        if (!comment.getChildren().isEmpty()) {
            comment.softDelete();
        } else {
            libraryPostCommentJpaRepository.delete(comment);
        }
    }

    private LibraryPost getPostById(Long postId) {
        return libraryPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.LIBRARY_POST_NOT_EXIST));
    }

    private void validateAuthority(LibraryPostComment comment, Member member) {
        boolean isOwner = comment.getWriterId().equals(member.getMemberId());
        boolean isAdminOrOps = member.getRole() == Role.ROLE_ADMIN || member.getRole() == Role.ROLE_OPS;
        if (!(isOwner || isAdminOrOps)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_DELETE);
        }
    }
}

