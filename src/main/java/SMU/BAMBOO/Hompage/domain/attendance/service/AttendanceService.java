package SMU.BAMBOO.Hompage.domain.attendance.service;

import SMU.BAMBOO.Hompage.domain.attendance.dto.AttendanceRequestDTO;

public interface AttendanceService {
    void markAttendance(AttendanceRequestDTO.MarkAttendance request);
}
