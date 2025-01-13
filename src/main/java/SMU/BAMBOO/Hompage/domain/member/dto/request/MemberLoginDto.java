package SMU.BAMBOO.Hompage.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "로그인 요청 DTO")
public record MemberLoginDto(@Schema(description = "학번", example = "202510777") String studentId,
                             @Schema(description = "비밀번호", example = "1234") String password) {

    @Builder
    public MemberLoginDto(
            @JsonProperty("studentId") String studentId,
            @JsonProperty("password") String password) {
        this.studentId = studentId;
        this.password = password;
    }
}
