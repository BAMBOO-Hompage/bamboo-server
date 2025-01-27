package SMU.BAMBOO.Hompage.domain.subject.controller;

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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPS')")
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

    @GetMapping
    @Operation(summary = "과목 리스트 조회")
    public SuccessResponse<List<SubjectResponseDTO.GetOne>> findAll() {
        List<SubjectResponseDTO.GetOne> result = subjectService.findAll();
        return SuccessResponse.ok(result);
    }

    @PutMapping("/{subjectId}")
    @Operation(summary = "과목 수정")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPS')")
    public SuccessResponse<SubjectResponseDTO.Update> update(
            @PathVariable("subjectId") Long id,
            @Valid @RequestBody SubjectRequestDTO.Update request
    ) {
        SubjectResponseDTO.Update result = subjectService.update(id, request);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/{subjectId}")
    @Operation(summary = "과목 삭제")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPS')")
    public SuccessResponse<String> delete(
            @PathVariable("subjectId") Long id
    ) {
        subjectService.delete(id);
        return SuccessResponse.ok("과목 삭제에 성공했습니다.");
    }
}
