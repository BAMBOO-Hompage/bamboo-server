package SMU.BAMBOO.Hompage.global.mail.service;

import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.mail.dto.response.EmailVerificationResponse;
import SMU.BAMBOO.Hompage.global.redis.service.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private static final String AUTH_CODE_PREFIX = "AuthCode:";

    private final JavaMailSender mailSender;
    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private final TemplateEngine templateEngine;

    @Value("${MAIL_AUTH_CODE_EXPIRATION}")
    private long authCodeExpirationMillis;

    @Value("${MAIL_SENDER}")
    private String mailSenderAddress;

    public void sendMail(String toEmail, String title, String authCode) {
        try {
            MimeMessage emailForm = createEmailForm(toEmail, title, authCode);
            mailSender.send(emailForm);
            log.info("이메일 발송 성공: toEmail={}, title={}", toEmail, title);
        } catch (MessagingException | MailException e) {
            log.error("이메일 발송 실패: toEmail={}, title={}, error={}", toEmail, title, e.getMessage(), e);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAIL);
        }
    }

    private MimeMessage createEmailForm(String toEmail, String title, String authCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Thymeleaf 템플릿 처리
        Context context = new Context();
        context.setVariable("verificationCode", authCode);
        String htmlContent = templateEngine.process("verification-email", context);

        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(htmlContent, true);
        helper.setFrom(mailSenderAddress);

        log.info("발신자 이메일 주소: {}", mailSenderAddress);
        return message;
    }

    // 이메일 인증 코드 전송
    @Async("taskExecutor")
    public void sendVerificationCode(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }

        String title = "BAMBOO 이메일 인증 번호";
        String authCode = generateAuthCode();

        sendMail(email, title, authCode);

        // Redis에 인증 코드 저장 (key: AuthCode + email, value: authCode)
        redisService.setValues(
                AUTH_CODE_PREFIX + email,
                authCode,
                Duration.ofMillis(authCodeExpirationMillis)
        );

        log.info("이메일 인증 코드 전송 성공: {}", email);
    }

    // 이메일 인증 코드 검증
    public EmailVerificationResponse verifyCode(String email, String authCode) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }

        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);

        if (redisAuthCode == null || !redisAuthCode.equals(authCode)) {
            log.warn("이메일 인증 실패: email={}, 입력 코드={}, Redis 코드={}", email, authCode, redisAuthCode);
            throw new CustomException(ErrorCode.AUTH_FAIL);
        }

        log.info("이메일 인증 성공: email={}", email);
        return EmailVerificationResponse.of(true);
    }

    // 랜덤 인증 코드 6자리 생성
    private String generateAuthCode() {
        return new SecureRandom().ints(6, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}
