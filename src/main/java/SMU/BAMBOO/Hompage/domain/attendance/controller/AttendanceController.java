package SMU.BAMBOO.Hompage.domain.attendance.controller;

import SMU.BAMBOO.Hompage.domain.attendance.dto.AttendanceRequestDTO;
import SMU.BAMBOO.Hompage.domain.attendance.service.AttendanceService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@Tag(name = "출석 관리 API", description = "출석 관리 API")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/mark")
    @Operation(summary = "출석 기록 저장 (스터디 ID, 주차 포함)")
    public SuccessResponse<String> markAttendance(
            @RequestBody @Valid AttendanceRequestDTO.MarkAttendance request
    ) {
        attendanceService.markAttendance(request);
        return SuccessResponse.ok("출석이 기록되었습니다.");
    }
}
