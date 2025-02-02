package SMU.BAMBOO.Hompage.domain.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "태그 관련 요청 DTO")
public class TagRequestDTO {

    @Schema(description = "태그 생성 요청 DTO")
    public record Create(
            @Schema(description = "태그 이름", example = "CV") String name
    ) {}

}
