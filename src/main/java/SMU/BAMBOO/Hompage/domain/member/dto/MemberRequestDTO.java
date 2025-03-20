package SMU.BAMBOO.Hompage.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "회원 관련 요청 DTO")
public class MemberRequestDTO {

    @Schema(description = "로그인 요청 DTO")
    public record Login (
            @Schema(description = "학번", example = "202510777") String studentId,
            @Schema(description = "비밀번호", example = "1234") String password
    ) {}

    @Schema(description = "회원가입 요청 DTO")
    public record SignUp (
            @Schema(description = "이메일", example = "user@gmail.com") String email,
            @Schema(description = "비밀번호", example = "1234") String password,
            @Schema(description = "이름", example = "김진석") String name,
            @Schema(description = "학과", example = "휴먼지능정보공학과") String major,
            @Schema(description = "학번", example = "202510777") String studentId,
            @Schema(description = "전화번호", example = "01012345678") String phoneNumber
    ) {}

    @Schema(description = "비밀번호 초기화 요청 DTO")
    public record ResetPw (
            @Schema(description = "학번", example = "202510777") String studentId,
            @Schema(description = "새 비밀번호", example = "9999") String newPassword1,
            @Schema(description = "새 비밀번호 확인", example = "9999") String newPassword2
    ) {}

    @Schema(description = "프로필 변경 요청 DTO")
    public record UpdateProfile (
            @Schema(description = "수정할 전화번호", example = "01088889999") String phoneNumber,
            @Schema(description = "수정할 이미지") MultipartFile profileImage
    ) {}

    @Schema(description = "비밀번호 변경 요청 DTO")
    public record UpdatePw (
            @Schema(description = "현재 비밀번호", example = "1234") String password,
            @Schema(description = "새 비밀번호", example = "9999") String newPassword
    ) {}

    @Schema(description = "권한 변경 요청 DTO")
    public record UpdateRole (
            @Schema(description = "변경할 유저 ID", example = "1") Long memberId,
            @Schema(description = "변경할 Role", example = "ROLE_ADMIN") String role
    ) {}
}
