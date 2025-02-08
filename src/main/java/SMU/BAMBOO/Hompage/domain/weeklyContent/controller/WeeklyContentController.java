package SMU.BAMBOO.Hompage.domain.weeklyContent.controller;

import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.weeklyContent.dto.WeeklyContentRequestDTO;
import SMU.BAMBOO.Hompage.domain.weeklyContent.dto.WeeklyContentResponseDTO;
import SMU.BAMBOO.Hompage.domain.weeklyContent.service.WeeklyContentService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects/{subjectId}/weeklyContents")
@RequiredArgsConstructor
@Tag(name = "주차별 내용(커리큘럼) API")
public class WeeklyContentController {

    private final WeeklyContentService weeklyContentService;

    @PostMapping
    @Operation(summary = "주차별 내용 생성")
    public SuccessResponse<WeeklyContentResponseDTO.Create> create(
            @PathVariable("subjectId") Long subjectId,
            @CurrentMember Member member,
            @Valid @RequestBody WeeklyContentRequestDTO.Create request
    ) {
        WeeklyContentResponseDTO.Create result = weeklyContentService.create(member.getMemberId(), subjectId ,request);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/{weeklyContentId}")
    @Operation(summary = "ID로 주차별 내용 단건 조회")
    public SuccessResponse<WeeklyContentResponseDTO.GetOne> findById(
            @PathVariable("subjectId") Long subjectId,
            @PathVariable("weeklyContentId") Long id
    ) {
        WeeklyContentResponseDTO.GetOne result = weeklyContentService.getById(id);
        return SuccessResponse.ok(result);
    }

    @GetMapping
    @Operation(summary = "특정 과목의 주차별 내용 전체 조회")
    public SuccessResponse<List<WeeklyContentResponseDTO.GetOne>> findAll(
            @PathVariable("subjectId") Long subjectId
    ) {
        List<WeeklyContentResponseDTO.GetOne> result = weeklyContentService.getWeeklyContentBySubjectId(subjectId);
        return SuccessResponse.ok(result);
    }

    @PatchMapping("/{weeklyContentId}")
    @Operation(summary = "주차별 내용 수정")
    public SuccessResponse<WeeklyContentResponseDTO.Update> update(
            @PathVariable("subjectId") Long subjectId,
            @PathVariable("weeklyContentId") Long id,
            @Valid @RequestBody WeeklyContentRequestDTO.Update request
    ) {
        WeeklyContentResponseDTO.Update result = weeklyContentService.update(id, subjectId, request);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/{weeklyContentId}")
    @Operation(summary = "주차별 내용 삭제")
    public SuccessResponse<String> delete(
            @PathVariable("subjectId") Long subjectId,
            @PathVariable("weeklyContentId") Long id
    ) {
        weeklyContentService.delete(id, subjectId);
        return SuccessResponse.ok("주차별 내용 삭제에 성공했습니다.");
    }
}
