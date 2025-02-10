package SMU.BAMBOO.Hompage.domain.enums;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum KnowledgeType {
    RESOURCES("학습 자료"),
    INSIGHTS("기술 트렌드 및 뉴스"),
    CAREERS("커리어 및 취업 정보");

    private final String description;

    KnowledgeType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static KnowledgeType from(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return Arrays.stream(KnowledgeType.values())
                .filter(type -> type.name().equalsIgnoreCase(value) || type.description.equals(value))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.KNOWLEDGE_INVALID_TYPE));
    }
}
