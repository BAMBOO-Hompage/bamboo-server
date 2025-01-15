package SMU.BAMBOO.Hompage.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "회원가입 요청 DTO")
public record MemberSignUpDto(@Schema(description = "이메일", example = "user@gmail.com") String email,
                             @Schema(description = "비밀번호", example = "1234") String password,
                              @Schema(description = "이름", example = "김진석") String name,
                              @Schema(description = "학과", example = "휴먼지능정보공학과") String major,
                              @Schema(description = "학번", example = "202510777") String studentId,
                              @Schema(description = "전화번호", example = "01012345678") String phoneNumber) {

    @Builder
    public MemberSignUpDto(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("name") String name,
            @JsonProperty("major") String major,
            @JsonProperty("studentId") String studentId,
            @JsonProperty("phoneNumber") String phoneNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.major = major;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
    }
}
