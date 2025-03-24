package SMU.BAMBOO.Hompage.domain.libraryPostComment.controller;

import SMU.BAMBOO.Hompage.domain.libraryPostComment.service.LibraryPostCommentService;
import SMU.BAMBOO.Hompage.domain.libraryPostComment.dto.LibraryPostCommentRequestDTO;
import SMU.BAMBOO.Hompage.domain.libraryPostComment.dto.LibraryPostCommentResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "알렉산드리아 댓글 API")
@RequestMapping("/api/library-posts/{libraryPostId}/comments")
public class LibraryPostCommentController {

    private final LibraryPostCommentService libraryPostCommentService;

    @PostMapping
    @Operation(summary = "댓글 작성")
    public SuccessResponse<LibraryPostCommentResponseDTO.Create> createComment(
            @PathVariable Long libraryPostId,
            @Valid @RequestBody LibraryPostCommentRequestDTO.Create request,
            @CurrentMember Member member
    ) {
        return SuccessResponse.ok(libraryPostCommentService.createComment(libraryPostId, request, member));
    }

    @GetMapping
    @Operation(summary = "댓글 목록 조회")
    public SuccessResponse<List<LibraryPostCommentResponseDTO.GetOne>> getComments(
            @PathVariable Long libraryPostId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return SuccessResponse.ok(libraryPostCommentService.getCommentsByPostId(libraryPostId, pageable).getContent());
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정")
    public SuccessResponse<List<LibraryPostCommentResponseDTO.GetOne>> updateComment(
            @PathVariable Long libraryPostId,
            @PathVariable Long commentId,
            @Valid @RequestBody LibraryPostCommentRequestDTO.Update request,
            @CurrentMember Member member
    ) {
        return SuccessResponse.ok(libraryPostCommentService.updateComment(libraryPostId, commentId, request, member));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제")
    public SuccessResponse<String> deleteComment(
            @PathVariable Long libraryPostId,
            @PathVariable Long commentId,
            @CurrentMember Member member
    ) {
        libraryPostCommentService.deleteComment(libraryPostId, commentId, member);
        return SuccessResponse.ok("댓글 삭제에 성공했습니다.");
    }
}
