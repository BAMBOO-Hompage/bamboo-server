package SMU.BAMBOO.Hompage.global.mail.service;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.mail.dto.response.EmailVerificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailFacade {

    private final MailService mailService;
    private final AuthCodeService authCodeService;

    @Async("taskExecutor")
    public void sendVerificationEmail(String email) {
        String authCode = authCodeService.generateAuthCode();
        try {
            mailService.sendMail(email, "BAMBOO 이메일 인증 번호", authCode);
            authCodeService.storeAuthCode(email, authCode);
        } catch (Exception e) {
            log.error("이메일 전송 실패: email={}, error={}", email, e.getMessage(), e);
            authCodeService.deleteAuthCode(email);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAIL);
        }
    }

    public EmailVerificationResponse verifyAuthCode(String email, String authCode) {
        return authCodeService.verifyCode(email, authCode);
    }
}