package SMU.BAMBOO.Hompage.global.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailSenderAddress;

    /** 메일 전송 */
    public void sendMail(String toEmail, String title, String authCode) {
        try {
            MimeMessage message = createEmailForm(toEmail, title, authCode);
            mailSender.send(message);
            log.info("이메일 전송 성공: {}", toEmail);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: email={}, error={}", toEmail, e.getMessage(), e);
        }
    }

    /** 전송할 메일 폼 생성 */
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

        return message;
    }

}
