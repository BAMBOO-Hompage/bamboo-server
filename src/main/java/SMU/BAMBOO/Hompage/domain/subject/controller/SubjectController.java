package SMU.BAMBOO.Hompage.domain.subject.controller;

import SMU.BAMBOO.Hompage.domain.study.dto.StudyResponseDTO;
import SMU.BAMBOO.Hompage.domain.subject.dto.SubjectRequestDTO;
import SMU.BAMBOO.Hompage.domain.subject.dto.SubjectResponseDTO;
import SMU.BAMBOO.Hompage.domain.subject.service.SubjectService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@Tag(name = "과목 API")
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    @Operation(summary = "과목 생성")
    public SuccessResponse<SubjectResponseDTO.Create> create(
            @Valid @RequestBody SubjectRequestDTO.Create request) {
        SubjectResponseDTO.Create result = subjectService.create(request);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/{subjectId}")
    @Operation(summary = "과목 ID로 단건 조회")
    public SuccessResponse<SubjectResponseDTO.GetOne> getOne(
            @PathVariable("subjectId") Long id
    ) {
        SubjectResponseDTO.GetOne result = subjectService.getById(id);
        return SuccessResponse.ok(result);
    }

    /**
     * 과목 리스트 조회
     * isBook = null > 전체 과목 조회
     * isBook = true > 커리큘럼 과목 조회
     * isBook = false > 자율 과목 조회
     */
    @GetMapping
    @Operation(summary = "과목 리스트 조회 (isBook이 true면 커러큘럼 조회/ false면 자율 조회/ null로 보내면 전체 조회)")
    public SuccessResponse<List<SubjectResponseDTO.GetOne>> findAll(
            @RequestParam(value = "커리큘럼 유무", required = false) Boolean isBook,
            @RequestParam(value = "기수", required = false) int batch) {

        List<SubjectResponseDTO.GetOne> result = subjectService.findAll(isBook, batch);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/{subjectId}/studies")
    @Operation(summary = "과목별 스터디 조회")
    public SuccessResponse<List<StudyResponseDTO.GetOne>> getStudiesBySubject(
            @PathVariable("subjectId") Long subjectId) {
        List<StudyResponseDTO.GetOne> result = subjectService.getStudiesBySubject(subjectId);
        return SuccessResponse.ok(result);
    }

    @PutMapping("/{subjectId}")
    @Operation(summary = "과목 수정")
    public SuccessResponse<SubjectResponseDTO.Update> update(
            @PathVariable("subjectId") Long id,
            @Valid @RequestBody SubjectRequestDTO.Update request
    ) {
        SubjectResponseDTO.Update result = subjectService.update(id, request);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/{subjectId}")
    @Operation(summary = "과목 삭제")
    public SuccessResponse<String> delete(
            @PathVariable("subjectId") Long id
    ) {
        subjectService.delete(id);
        return SuccessResponse.ok("과목 삭제에 성공했습니다.");
    }
}
