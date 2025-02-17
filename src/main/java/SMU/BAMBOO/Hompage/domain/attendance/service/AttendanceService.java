package SMU.BAMBOO.Hompage.domain.attendance.service;

import SMU.BAMBOO.Hompage.domain.attendance.dto.AttendanceRequestDTO;
import SMU.BAMBOO.Hompage.domain.attendance.dto.AttendanceResponseDTO;

public interface AttendanceService {
    void markAttendance(AttendanceRequestDTO.MarkAttendance request);
    AttendanceResponseDTO.GetAttendanceBoard getAttendanceBoard(AttendanceRequestDTO.MarkAttendance request);
}
