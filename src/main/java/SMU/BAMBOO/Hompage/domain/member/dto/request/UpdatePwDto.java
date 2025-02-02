package SMU.BAMBOO.Hompage.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "비밀번호 변경 요청 DTO")
public record UpdatePwDto(@Schema(description = "현재 비밀번호", example = "1234") String password,
                          @Schema(description = "새 비밀번호", example = "9999") String newPassword) {

    @Builder
    public UpdatePwDto(
            @JsonProperty("password") String password,
            @JsonProperty("newPassword") String newPassword ) {
        this.password = password;
        this.newPassword = newPassword;
    }
}
