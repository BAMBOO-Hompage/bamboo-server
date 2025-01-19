package SMU.BAMBOO.Hompage.global.jwt.util;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.jwt.userDetails.CustomUserDetails;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessExpMs;
    private final Long refreshExpMs;
    private final StringRedisTemplate redisTemplate;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token.access-expiration-time}") Long accessExpMs,
            @Value("${jwt.token.refresh-expiration-time}") Long refreshExpMs,
            StringRedisTemplate redisTemplate
    ) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.accessExpMs = accessExpMs;
        this.refreshExpMs = refreshExpMs;
        this.redisTemplate = redisTemplate;
    }

    // Access Token 생성
    public String createAccessToken(String studentId, String role) {
        return createJwtToken(studentId, role, accessExpMs);
    }

    // Refresh Token 생성
    public String createRefreshToken(CustomUserDetails customUserDetails) {
        String authorities = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String refreshToken = createJwtToken(customUserDetails.getUsername(), authorities, refreshExpMs);

        storeRefreshToken(customUserDetails.getUsername(), refreshToken);
        return refreshToken;
    }


    // JWT 토큰 생성 공통 로직
    private String createJwtToken(String subject, String role, Long expirationMs) {
        Instant issuedAt = Instant.now();
        Instant expiration = issuedAt.plusMillis(expirationMs);

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(subject)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpMs))
                .signWith(secretKey)
                .compact();
    }

    // Redis에 Refresh Token 저장
    public void storeRefreshToken(String studentId, String refreshToken) {
        String key = getRefreshTokenKey(studentId);
        try {
            redisTemplate.opsForValue().set(key, refreshToken, refreshExpMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Failed to store Refresh Token in Redis", e);
            throw new CustomException(ErrorCode.REDIS_STORE_FAILED);
        }
    }

    // Redis 에서 Refresh Token 조회
    public String getRefreshTokenFromRedis(String studentId) {
        return redisTemplate.opsForValue().get(getRefreshTokenKey(studentId));
    }

    // Redis 에서 Refresh Token 삭제
    public void deleteRefreshToken(String studentId) {
        redisTemplate.delete(getRefreshTokenKey(studentId));
    }

    // Redis 조회용 키 생성
    private String getRefreshTokenKey(String studentId) {
        return "refresh_token:" + studentId;
    }

    // 사용자 Student ID 추출
    public String getStudentId(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.ACCESS_TOKEN_INVALID);
        }
    }

    // JWT에서 Role 추출
    public String getRole(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("role", String.class);
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.ACCESS_TOKEN_INVALID);
        }
    }

    // Request에서 Access Token 추출
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            long seconds = 3 * 60;
            boolean isExpired = Jwts
                    .parser()
                    .clockSkewSeconds(seconds)
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());

            return !isExpired;
        } catch (JwtException e) {
            return false;
        }
    }

    // Refresh Token으로 새로운 Access Token 재발급
    public String reissueToken(String refreshToken) {
        String studentId = getStudentId(refreshToken);
        String role = getRole(refreshToken);
        return createAccessToken(studentId, role);
    }

    // Access Token 생성 로직
    public String createAccessToken(String studentId) {
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(studentId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpMs))
                .signWith(secretKey)
                .compact();
    }

    public long getExpiration(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .getTime(); // 만료 시간을 밀리초로 반환
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }
    }
}
