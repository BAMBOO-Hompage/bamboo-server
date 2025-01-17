package SMU.BAMBOO.Hompage.global.mail.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "이메일 인증 확인 요청 DTO")
public record VerifyEmailDto(@Schema(description = "이메일", example = "user@naver.com") String email,
                             @Schema(description = "인증번호", example = "XXXXXX") String authcode) {

    @Builder
    public VerifyEmailDto(
            @JsonProperty("email") String email,
            @JsonProperty("authcode") String authcode) {
        this.email = email;
        this.authcode = authcode;
    }
}
