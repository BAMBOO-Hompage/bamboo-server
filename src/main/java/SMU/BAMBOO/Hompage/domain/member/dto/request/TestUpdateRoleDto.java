package SMU.BAMBOO.Hompage.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "임원 권한이 필요없는 테스트용 권한 변경 요청 DTO")
public record TestUpdateRoleDto(@Schema(description = "변경할 유저 ID", example = "1") Long memberId,
                            @Schema(description = "변경할 Role", example = "ROLE_ADMIN") String role) {

    @Builder
    public TestUpdateRoleDto(
            @JsonProperty("memberId") Long memberId,
            @JsonProperty("role") String role
    ) {
        this.memberId = memberId;
        this.role = role;
    }
}