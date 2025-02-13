package SMU.BAMBOO.Hompage.domain.mapping.memberStudy.controller;

import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.dto.MemberStudyResponseDTO;
import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.service.MemberStudyService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members/{memberId}/studies/{studyId}")
@RequiredArgsConstructor
@Tag(name = "출석 관련 API")
public class MemberStudyController {

    private final MemberStudyService memberStudyService;

    @PostMapping("/attendance")
    @Operation(summary = "출석 처리")
    public SuccessResponse<MemberStudyResponseDTO.Detail> attendance(@PathVariable Long memberId, @PathVariable Long studyId) {
        MemberStudyResponseDTO.Detail result = memberStudyService.attendance(memberId, studyId);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/absence")
    @Operation(summary = "결석 처리")
    public SuccessResponse<MemberStudyResponseDTO.Detail> absence(@PathVariable Long memberId, @PathVariable Long studyId) {
        MemberStudyResponseDTO.Detail result = memberStudyService.absence(memberId, studyId);
        return SuccessResponse.ok(result);
    }
}
