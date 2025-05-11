package SMU.BAMBOO.Hompage.domain.knowledgeComment.controller;

import SMU.BAMBOO.Hompage.domain.knowledgeComment.dto.KnowledgeCommentRequestDTO;
import SMU.BAMBOO.Hompage.domain.knowledgeComment.dto.KnowledgeCommentResponseDTO;
import SMU.BAMBOO.Hompage.domain.knowledgeComment.service.KnowledgeCommentService;
import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Builder
@Tag(name = "지식 공유 게시판 댓글 API")
@RequestMapping("/api/knowledges/{knowledgeId}/comments")
public class KnowledgeCommentController {

    private final KnowledgeCommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 작성")
    public SuccessResponse<KnowledgeCommentResponseDTO.Create> createComment(
            @PathVariable("knowledgeId") Long knowledgeId,
            @Valid @RequestBody KnowledgeCommentRequestDTO.Create request,
            @CurrentMember Member member
    ) {
        KnowledgeCommentResponseDTO.Create result = commentService.createComment(knowledgeId, request, member);
        return SuccessResponse.ok(result);
    }

    @GetMapping
    @Operation(summary = "댓글 목록 조회")
    public SuccessResponse<KnowledgeCommentResponseDTO.PagedGet> getComments(
            @PathVariable("knowledgeId") Long knowledgeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<KnowledgeCommentResponseDTO.GetOne> result = commentService.getCommentsByKnowledgeId(knowledgeId, pageable);
        return SuccessResponse.ok(KnowledgeCommentResponseDTO.PagedGet.from(result));
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정")
    public SuccessResponse<List<KnowledgeCommentResponseDTO.GetOne>> updateComment(
            @PathVariable("knowledgeId") Long knowledgeId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody KnowledgeCommentRequestDTO.Update request,
            @CurrentMember Member member
    ) {
        List<KnowledgeCommentResponseDTO.GetOne> result = commentService.updateComment(knowledgeId, commentId, request, member);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제")
    public SuccessResponse<String> deleteComment(
            @PathVariable("knowledgeId") Long knowledgeId,
            @PathVariable("commentId") Long commentId,
            @CurrentMember Member member
    ) {
        commentService.deleteComment(knowledgeId, commentId, member);
        return SuccessResponse.ok("댓글 삭제에 성공했습니다.");
    }
}
