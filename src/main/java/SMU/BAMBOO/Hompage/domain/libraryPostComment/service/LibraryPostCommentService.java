package SMU.BAMBOO.Hompage.domain.libraryPostComment.service;

import SMU.BAMBOO.Hompage.domain.libraryPostComment.dto.LibraryPostCommentRequestDTO;
import SMU.BAMBOO.Hompage.domain.libraryPostComment.dto.LibraryPostCommentResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LibraryPostCommentService {
    LibraryPostCommentResponseDTO.Create createComment(Long postId, LibraryPostCommentRequestDTO.Create request, Member member);
    Page<LibraryPostCommentResponseDTO.GetOne> getCommentsByPostId(Long postId, Pageable pageable);
    List<LibraryPostCommentResponseDTO.GetOne> updateComment(Long postId, Long commentId, LibraryPostCommentRequestDTO.Update request, Member member);
    void deleteComment(Long postId, Long commentId, Member member);
}