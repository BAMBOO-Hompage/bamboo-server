package SMU.BAMBOO.Hompage.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

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
}
