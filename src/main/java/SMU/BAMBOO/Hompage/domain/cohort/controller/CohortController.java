package SMU.BAMBOO.Hompage.domain.cohort.controller;

import SMU.BAMBOO.Hompage.domain.cohort.dto.CohortRequestDTO;
import SMU.BAMBOO.Hompage.domain.cohort.dto.CohortResponseDTO;
import SMU.BAMBOO.Hompage.domain.cohort.service.CohortService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cohorts")
@RequiredArgsConstructor
@Tag(name = "기수 API")
public class CohortController {

    private final CohortService cohortService;

    @PostMapping
    @Operation(summary = "기수 등록")
    public SuccessResponse<CohortResponseDTO.Create> create(@Valid @RequestBody CohortRequestDTO.Create request) {
        CohortResponseDTO.Create result = cohortService.create(request);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/{cohortId}")
    @Operation(summary = "ID로 기수 조회")
    public SuccessResponse<CohortResponseDTO.GetOne> getOne(@PathVariable("cohortId") Long id) {
        CohortResponseDTO.GetOne result = cohortService.getById(id);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/batch/{batch}")
    @Operation(summary = "기수(batch)로 조회")
    public SuccessResponse<CohortResponseDTO.GetOne> getByBatch(@PathVariable("batch") int batch) {
        CohortResponseDTO.GetOne result = cohortService.getByBatch(batch);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/all")
    @Operation(summary = "기수 전체 조회")
    public SuccessResponse<List<CohortResponseDTO.GetOne>> findAll() {
        List<CohortResponseDTO.GetOne> result = cohortService.findAll();
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/{cohortId}")
    @Operation(summary = "기수 삭제")
    public SuccessResponse<String> delete(@PathVariable("cohortId") Long id) {
        cohortService.delete(id);
        return SuccessResponse.ok("기수 삭제에 성공했습니다.");
    }
}
