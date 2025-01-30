package SMU.BAMBOO.Hompage.domain.study.controller;

import SMU.BAMBOO.Hompage.domain.study.dto.StudyRequestDTO;
import SMU.BAMBOO.Hompage.domain.study.dto.StudyResponseDTO;
import SMU.BAMBOO.Hompage.domain.study.service.StudyService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
@Tag(name = "스터디 API")
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    @Operation(summary = "스터디 생성")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPS')")
    public SuccessResponse<StudyResponseDTO.Create> create(
            @Valid @RequestBody StudyRequestDTO.Create request
    ) {
        StudyResponseDTO.Create result = studyService.create(request);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/{studyId}")
    @Operation(summary = "스터디 ID로 단건 조회")
    public SuccessResponse<StudyResponseDTO.GetOne> getOne(
            @PathVariable("studyId") Long id
    ) {
        StudyResponseDTO.GetOne result = studyService.getById(id);
        return SuccessResponse.ok(result);
    }

    @GetMapping
    @Operation(summary = "스터디 목록 조회")
    public SuccessResponse<List<StudyResponseDTO.GetOne>> findAll() {
        List<StudyResponseDTO.GetOne> result = studyService.findAll();
        return SuccessResponse.ok(result);
    }

    @PatchMapping("/{studyId}")
    @Operation(summary = "스터디 정보 수정")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPS')")
    public SuccessResponse<StudyResponseDTO.Update> update(
            @PathVariable("studyId") Long id,
            @Valid @RequestBody StudyRequestDTO.Update request
    ) {
        StudyResponseDTO.Update result = studyService.update(id, request);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/{studyId}")
    @Operation(summary = "스터디 삭제")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPS')")
    public SuccessResponse<String> delete(
            @PathVariable("studyId") Long id
    ) {
        studyService.delete(id);
        return SuccessResponse.ok("스터디 삭제에 성공했습니다.");
    }
}
