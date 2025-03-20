package SMU.BAMBOO.Hompage.domain.inventory.controller;

import SMU.BAMBOO.Hompage.domain.inventory.dto.InventoryRequestDTO;
import SMU.BAMBOO.Hompage.domain.inventory.dto.InventoryResponseDTO;
import SMU.BAMBOO.Hompage.domain.inventory.service.InventoryService;
import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
@Tag(name = "스터디 정리본 API")
public class InventoryController {

    private final InventoryService inventoryService;

    @RequestBody(content = @Content(
            encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE))) // request 내부에 Content Type 이 없으면 오류남. 그래서 application/json 설정
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "스터디 정리본 생성")
    public SuccessResponse<InventoryResponseDTO.Create> create(
            @CurrentMember Member member,
            @RequestPart(value = "request") InventoryRequestDTO.Create request,
            @RequestPart(required = false) MultipartFile file
    ) {
        InventoryResponseDTO.Create result = inventoryService.create(member.getMemberId(), request, file);
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
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<InventoryResponseDTO.GetOne> result = inventoryService.getInventoriesByStudy(studyId, page-1, size);
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
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<InventoryResponseDTO.GetOne> result = inventoryService.getInventories(page-1, size);
        return SuccessResponse.ok(result);
    }

    @RequestBody(content = @Content(
            encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE)))
    @PatchMapping(value = "/{inventoriesId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "스터디 정리본 수정")
    public SuccessResponse<InventoryResponseDTO.Update> update(
            @PathVariable("inventoriesId") Long id,
            @RequestPart(value = "request") InventoryRequestDTO.Update request,
            @RequestPart(required = false) MultipartFile file
    ) {
        InventoryResponseDTO.Update result = inventoryService.update(id, request, file);
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

    @GetMapping("/members/{memberId}/week/{week}")
    @Operation(summary = "회원 ID와 주차 정보로 스터디 정리본 조회")
    public SuccessResponse<InventoryResponseDTO.GetOne> getInventoriesByMemberAndWeek(
            @PathVariable("memberId") Long memberId,
            @PathVariable("week") int week
    ) {
        InventoryResponseDTO.GetOne result = inventoryService.getInventoriesByMemberAndWeek(memberId, week);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/weekly-best")
    @Operation(summary = "주간 베스트 선정")
    public SuccessResponse<String> selectWeeklyBest(
            @RequestParam("studyId") Long studyId,
            @RequestParam("week") int week,
            @RequestParam("memberId") Long memberId
    ) {
        inventoryService.setWeeklyBest(studyId, week, memberId);
        return SuccessResponse.ok("주간 베스트 선정에 성공하였습니다.");
    }

    @GetMapping("/weekly-best/{studyId}/{week}")
    @Operation(summary = "주차별 주간 베스트 정리본 조회")
    public SuccessResponse<InventoryResponseDTO.GetOne> getWeeklyBestInventory(
            @PathVariable("studyId") Long studyId,
            @PathVariable("week") int week
    ) {
        InventoryResponseDTO.GetOne result = inventoryService.getWeeklyBestInventory(studyId, week);
        return SuccessResponse.ok(result);
    }
}
