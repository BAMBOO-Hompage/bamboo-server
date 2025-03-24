package SMU.BAMBOO.Hompage.global.mail.service;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.mail.dto.response.EmailVerificationResponse;
import SMU.BAMBOO.Hompage.global.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthCodeService {

    private static final String AUTH_CODE_PREFIX = "AuthCode:";

    private final RedisService redisService;

    @Value("${spring.mail.mail_auth_code_expiration}")
    private long authCodeExpirationMillis;

    /** 6자리 랜덤 코드 생성 */
    public String generateAuthCode() {
        return new SecureRandom().ints(6, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    /** Redis에 인증코드 저장 */
    public void storeAuthCode(String email, String authCode) {
        redisService.setValues(
                AUTH_CODE_PREFIX + email,
                authCode,
                Duration.ofMillis(authCodeExpirationMillis)
        );
    }

    /** Redis에 인증코드 삭제 */
    public void deleteAuthCode(String email) {
        redisService.deleteValues(AUTH_CODE_PREFIX + email);
    }

    /** 인증코드와 사용자 입력 값 검증 */
    public EmailVerificationResponse verifyCode(String email, String authCode) {
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        if (redisAuthCode == null || !redisAuthCode.equals(authCode)) {
            log.warn("이메일 인증 실패: email={}, 입력 코드={}, Redis 코드={}", email, authCode, redisAuthCode);
            throw new CustomException(ErrorCode.AUTH_FAIL);
        }
        log.info("이메일 인증 성공: email={}", email);
        return EmailVerificationResponse.of(true);
    }
}
