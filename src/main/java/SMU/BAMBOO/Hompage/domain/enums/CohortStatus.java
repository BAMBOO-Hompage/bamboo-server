package SMU.BAMBOO.Hompage.domain.enums;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CohortStatus {
    INACTIVE("활동 종료"),
    ACTIVE("활동 중"),
    PREPARING("활동 준비");

    private final String description;

    public static CohortStatus from(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return Arrays.stream(CohortStatus.values())
                .filter(type -> type.name().equalsIgnoreCase(value) || type.description.equals(value))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.COHORT_INVALID_TYPE));
    }
}
