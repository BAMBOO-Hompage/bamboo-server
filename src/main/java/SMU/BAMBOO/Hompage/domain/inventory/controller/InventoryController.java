package SMU.BAMBOO.Hompage.domain.inventory.controller;

import SMU.BAMBOO.Hompage.domain.inventory.dto.InventoryRequestDTO;
import SMU.BAMBOO.Hompage.domain.inventory.dto.InventoryResponseDTO;
import SMU.BAMBOO.Hompage.domain.inventory.service.InventoryService;
import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
@Tag(name = "스터디 정리본 API")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @Operation(summary = "스터디 정리본 생성")
    public SuccessResponse<InventoryResponseDTO.Create> create(
            @CurrentMember Member member,
            @Valid @RequestBody InventoryRequestDTO.Create request
    ) {
        InventoryResponseDTO.Create result = inventoryService.create(member.getMemberId(), request);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/{inventoriesId}")
    @Operation(summary = "ID로 스터디 정리본 단건 조회")
    public SuccessResponse<InventoryResponseDTO.GetOne> findById(
            @PathVariable("inventoriesId") Long id
    ) {
        InventoryResponseDTO.GetOne result = inventoryService.getById(id);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/study/{studyId}")
    @Operation(summary = "스터디별 스터디 정리본 페이지 조회")
    public SuccessResponse<Page<InventoryResponseDTO.GetOne>> getInventoriesByStudy(
            @PathVariable("studyId") Long studyId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<InventoryResponseDTO.GetOne> result = inventoryService.getInventoriesByStudy(studyId, page, size);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/all")
    @Operation(summary = "스터디 정리본 전체 조회")
    public SuccessResponse<List<InventoryResponseDTO.GetOne>> findAll() {
        List<InventoryResponseDTO.GetOne> result = inventoryService.findAll();
        return SuccessResponse.ok(result);
    }

    @GetMapping
    @Operation(summary = "스터디 정리본 페이지 조회")
    public SuccessResponse<Page<InventoryResponseDTO.GetOne>> getInventories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<InventoryResponseDTO.GetOne> result = inventoryService.getInventories(page, size);
        return SuccessResponse.ok(result);
    }

    @PatchMapping("/{inventoriesId}")
    @Operation(summary = "스터디 정리본 수정")
    public SuccessResponse<InventoryResponseDTO.Update> update(
            @PathVariable("inventoriesId") Long id,
            @Valid @RequestBody InventoryRequestDTO.Update request
    ) {
        InventoryResponseDTO.Update result = inventoryService.update(id, request);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/{inventoriesId}")
    @Operation(summary = "스터디 정리본 삭제")
    public SuccessResponse<String> delete(
            @PathVariable("inventoriesId") Long id
    ) {
        inventoryService.delete(id);
        return SuccessResponse.ok("스터디 정리본 삭제에 성공했습니다.");
    }
}
