package SMU.BAMBOO.Hompage.domain.enums;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AttendanceStatus {
    PRESENT("출석"),
    ABSENT("결석");

    private final String description;

    public static AttendanceStatus from(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return Arrays.stream(AttendanceStatus.values())
                .filter(type -> type.name().equalsIgnoreCase(value) || type.description.equals(value))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ATTENDANCE_INVALID_TYPE));
    }
}
