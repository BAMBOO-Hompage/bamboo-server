package SMU.BAMBOO.Hompage.domain.enums;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NoticeType {
    EVENTS("대회 및 세미나"),
    NOTICE("동아리 공지");

    private final String description;

    NoticeType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static NoticeType from(String value) {
        return Arrays.stream(NoticeType.values())
                .filter(type -> type.name().equalsIgnoreCase(value.trim()) || type.description.equals(value.trim()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOTICE_INVALID_TYPE));
    }
}