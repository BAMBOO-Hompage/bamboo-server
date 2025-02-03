package SMU.BAMBOO.Hompage.domain.tag.dto;

import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "태그 관련 응답 DTO")
public class TagResponseDTO {

    @Schema(description = "태그 생성 응답 DTO")
    public record Create(
            Long tagId,
            String name
    ) {
        public static Create from(Tag tag) {
            return new Create(
                    tag.getTagId(),
                    tag.getName()
            );
        }
    }

    @Schema(description = "태그 조회 응답 DTO")
    public record GetOne(
            Long tagId,
            String name
    ) {
        public static GetOne from(Tag tag) {
            return new GetOne(
                    tag.getTagId(),
                    tag.getName()
            );
        }
    }
}
