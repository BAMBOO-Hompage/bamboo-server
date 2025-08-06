package SMU.BAMBOO.Hompage.domain.studyRecruitment.controller;

import SMU.BAMBOO.Hompage.domain.studyRecruitment.dto.StudyRecruitmentRequestDTO;
import SMU.BAMBOO.Hompage.domain.studyRecruitment.dto.StudyRecruitmentResponseDTO;
import SMU.BAMBOO.Hompage.domain.studyRecruitment.service.StudyRecruitmentService;
import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/study-recruitments")
@RequiredArgsConstructor
@Tag(name = "스터디 모집 API")
public class StudyRecruitmentController {

    private final StudyRecruitmentService studyRecruitmentService;

    @PostMapping
    @Operation(summary = "스터디 모집 게시글 생성")
    public SuccessResponse<StudyRecruitmentResponseDTO.Create> createStudyRecruitment(
            @Valid @RequestBody StudyRecruitmentRequestDTO.Create request,
            @CurrentMember Member member) {
        StudyRecruitmentResponseDTO.Create response = studyRecruitmentService.create(request, member);
        return SuccessResponse.ok(response);
    }

    @GetMapping
    @Operation(summary = "스터디 모집 게시글 목록 조회 (검색어 적용 가능)")
    public SuccessResponse<Page<StudyRecruitmentResponseDTO.GetOne>> getStudyRecruitments(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        keyword = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();
        Page<StudyRecruitmentResponseDTO.GetOne> list = studyRecruitmentService.getStudyRecruitments(keyword, page - 1, size);
        return SuccessResponse.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "스터디 모집 게시글 단일 조회")
    public SuccessResponse<StudyRecruitmentResponseDTO.GetOne> getStudyRecruitment(@PathVariable Long id) {
        StudyRecruitmentResponseDTO.GetOne response = studyRecruitmentService.getById(id);
        return SuccessResponse.ok(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "스터디 모집 게시글 수정")
    public SuccessResponse<StudyRecruitmentResponseDTO.Update> updateStudyRecruitment(
            @CurrentMember Member member,
            @PathVariable Long id,
            @Valid @RequestBody StudyRecruitmentRequestDTO.Update request) {
        StudyRecruitmentResponseDTO.Update result = studyRecruitmentService.update(member, id, request);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "스터디 모집 게시글 삭제")
    public SuccessResponse<String> deleteStudyRecruitment(
            @CurrentMember Member member,
            @PathVariable Long id) {
        studyRecruitmentService.delete(member, id);
        return SuccessResponse.ok("스터디 모집 게시글이 삭제되었습니다.");
    }
}
