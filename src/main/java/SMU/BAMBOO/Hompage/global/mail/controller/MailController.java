package SMU.BAMBOO.Hompage.global.mail.controller;

import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import SMU.BAMBOO.Hompage.global.mail.dto.request.VerifyEmailDto;
import SMU.BAMBOO.Hompage.global.mail.dto.response.EmailVerificationResponse;
import SMU.BAMBOO.Hompage.global.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
@Tag(name = "이메일")
public class MailController {
    private final MailService mailService;

    @PostMapping("/verification-requests")
    @Operation(summary = "인증코드 전송")
    public SuccessResponse<String> sendVerificationCode(String email) {

        mailService.sendVerificationCode(email);
        return SuccessResponse.ok(email + "에 인증코드를 전송하였습니다.");
    }

    @PostMapping("/verifications")
    @Operation(summary = "인증 코드 검증")
    public SuccessResponse<EmailVerificationResponse> verifyEmail(VerifyEmailDto request) {

        EmailVerificationResponse result = mailService.verifyCode(request.email(), request.authcode());
        return SuccessResponse.ok(result);
    }
}
