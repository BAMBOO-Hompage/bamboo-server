package SMU.BAMBOO.Hompage.domain.award.controller;

import SMU.BAMBOO.Hompage.domain.award.dto.AwardRequestDTO;
import SMU.BAMBOO.Hompage.domain.award.dto.AwardResponseDTO;
import SMU.BAMBOO.Hompage.domain.award.service.AwardService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/awards")
@RequiredArgsConstructor
@Tag(name = "명예의 전당 (Award) API")
public class AwardController {

    private final AwardService awardService;

    @PostMapping
    @Operation(summary = "명예의 전당 생성")
    public SuccessResponse<AwardResponseDTO.Create> create(
            @Valid @RequestBody AwardRequestDTO.Create request
    ) {
        AwardResponseDTO.Create result = awardService.create(request);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/{awardId}")
    @Operation(summary = "ID로 명예의 전당 단건 조회")
    public SuccessResponse<AwardResponseDTO.GetOne> findById(
            @PathVariable("awardId") Long id
    ) {
        AwardResponseDTO.GetOne result = awardService.getById(id);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/batch/{batchId}")
    @Operation(summary = "기수 기준 명예의 전당 조회")
    public SuccessResponse<List<AwardResponseDTO.GetOne>> getAwardsByBatch(
            @PathVariable("batchId") int batch
    ) {
        List<AwardResponseDTO.GetOne> result = awardService.getAwardsByBatch(batch);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/all")
    @Operation(summary = "명예의 전당 전체 조회")
    public SuccessResponse<List<AwardResponseDTO.GetOne>> findAll() {
        List<AwardResponseDTO.GetOne> result = awardService.findAll();
        return SuccessResponse.ok(result);
    }

    @PatchMapping("/{awardId}")
    @Operation(summary = "명예의 전당 수정")
    public SuccessResponse<AwardResponseDTO.Update> update(
            @PathVariable("awardId") Long id,
            @Valid @RequestBody AwardRequestDTO.Update request
    ) {
        AwardResponseDTO.Update result = awardService.update(id, request);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/{awardId}")
    @Operation(summary = "명예의 전당 삭제")
    public SuccessResponse<String> delete(
            @PathVariable("awardId") Long id
    ) {
        awardService.delete(id);
        return SuccessResponse.ok("어워드 삭제에 성공했습니다.");
    }

    @GetMapping("/awards/latest")
    @Operation(summary = "기수 기준 최신 주차의 명예의 전당 목록 조회")
    public SuccessResponse<List<AwardResponseDTO.GetOne>> getLatestWeekAwardsByBatch(
            @RequestParam int batch
    ) {
        List<AwardResponseDTO.GetOne> response = awardService.getLatestWeekAwardsByBatch(batch);
        return SuccessResponse.ok(response);
    }

}
