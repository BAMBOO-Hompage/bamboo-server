package SMU.BAMBOO.Hompage.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NoticeType {
    EVENTS("대회 및 세미나"),
    NOTICE("공지사항");

    private final String description;

    NoticeType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}