package SMU.BAMBOO.Hompage.domain.libraryPost.controller;

import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostRequestDTO;
import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostResponseDTO;
import SMU.BAMBOO.Hompage.domain.libraryPost.service.LibraryPostService;
import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Builder
@RestController
@RequestMapping("/api/library-posts")
@RequiredArgsConstructor
@Tag(name = "알렉산드리아 API")
public class LibraryPostController {

    private final LibraryPostService libraryPostService;

    @PostMapping
    @Operation(summary = "알렉산드리아에 글 등록")
    public SuccessResponse<LibraryPostResponseDTO.Create> create(
            @Valid @RequestBody LibraryPostRequestDTO.Create request,
            @CurrentMember Member member
    ) {
        LibraryPostResponseDTO.Create result = libraryPostService.create(request, member);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/{libraryPostId}")
    @Operation(summary = "알렉산드리아 글 ID로 단건 조회")
    public SuccessResponse<LibraryPostResponseDTO.GetOne> getOne(
            @PathVariable("libraryPostId") Long id
    ) {
        LibraryPostResponseDTO.GetOne result = libraryPostService.getById(id);
        return SuccessResponse.ok(result);
    }

    @GetMapping
    @Operation(summary = "알렉산드리아 글 목록 조회")
    public SuccessResponse<Page<LibraryPostResponseDTO.GetOne>> getLibraryPosts(
            @RequestParam(value = "tab", required = false, defaultValue = "paperName") String tab,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "15") int size
    ) {
        Page<LibraryPostResponseDTO.GetOne> result = libraryPostService.getLibraryPosts(tab, keyword, page, size);
        return SuccessResponse.ok(result);
    }

    @PutMapping("/{libraryPostId}")
    @Operation(summary = "알렉산드리아 글 수정")
    public SuccessResponse<String> update(
            @PathVariable("libraryPostId") Long id,
            @Valid @RequestBody LibraryPostRequestDTO.Update request
    ) {
        libraryPostService.update(id, request);
        return SuccessResponse.ok("알렉산드리아 글 수정에 성공했습니다.");
    }

    @DeleteMapping("/{libraryPostId}")
    @Operation(summary = "알렉산드리아 글 삭제")
    public SuccessResponse<String> delete(
            @PathVariable("libraryPostId") Long id
    ) {
        libraryPostService.delete(id);
        return SuccessResponse.ok("알렉산드리아 글 삭제에 성공했습니다.");
    }

    @PatchMapping("/{libraryPostId}/tags")
    @Operation(summary = "알렉산드리아 글의 태그 초기화")
    public SuccessResponse<LibraryPostResponseDTO.GetOne> resetTags(
            @PathVariable("libraryPostId") Long id,
            @Valid @RequestBody LibraryPostRequestDTO.ResetTag request
    ) {
        LibraryPostResponseDTO.GetOne result = libraryPostService.resetTags(id, request);
        return SuccessResponse.ok(result);
    }
}
