package SMU.BAMBOO.Hompage.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "비밀번호 초기화 요청 DTO")
public record ResetPwDto(
        @Schema(description = "학번", example = "202510777") String studentId,
        @Schema(description = "새 비밀번호", example = "9999") String newPassword1,
        @Schema(description = "새 비밀번호 확인", example = "9999") String newPassword2
) {
    @Builder
    public ResetPwDto(
            @JsonProperty("studentId") String studentId,
            @JsonProperty("newPassword1") String newPassword1,
            @JsonProperty("newPassword2") String newPassword2 ) {
        this.studentId = studentId;
        this.newPassword1 = newPassword1;
        this.newPassword2 = newPassword2;
    }
}