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
            @Value("${jwt.token.access-expiration-time}") Long access,
            @Value("${jwt.token.refresh-expiration-time}") Long refresh,
            StringRedisTemplate redisTemplate
    ) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessExpMs = access;
        this.refreshExpMs = refresh;
        this.redisTemplate = redisTemplate;
    }

    // Redis 에 Refresh Token 저장
    public void storeRefreshToken(String studentId, String refreshToken) {
        String key = getRefreshTokenKey(studentId);
        try {
            redisTemplate.opsForValue().set(key, refreshToken, refreshExpMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_STORE_FAILED);
        }
    }


    // Redis 에서 Refresh Token 조회
    public String getRefreshTokenFromRedis(String studentId) {
        return redisTemplate.opsForValue().get(getRefreshTokenKey(studentId));
    }

    // Redis 에서 토큰 조회용 키 생성
    private String getRefreshTokenKey(String studentId) {
        return "refresh_token:" + studentId;
    }

    // Redis 에서 토큰 삭제 - 로그아웃할 때 사용
    public void deleteRefreshToken(String studentId) {
        redisTemplate.delete(getRefreshTokenKey(studentId));
    }

    // 사용자 StudentID 추출
    public String getStudendId(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // JWT 토큰을 입력으로 받아 토큰의 claim 에서 사용자 권한 추출
    public String getRole(String token) throws SignatureException{
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
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
    public void validateToken(String token) {
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
            if (isExpired) {
                throw new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRED);
            }
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.ACCESS_TOKEN_INVALID);
        }
    }

    // 새로운 Access Token 생성
    public String reissueToken(String refreshToken) {
        // Refresh Token에서 사용자 ID 추출
        String studentId = getStudendId(refreshToken);

        // 새로운 Access Token 생성
        return createAccessToken(studentId);
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

    // Refresh Token 생성
    public String createRefreshToken(CustomUserDetails customUserDetails) {
        Instant expiration = Instant.now().plusMillis(refreshExpMs);
        String refreshToken = tokenProvider(customUserDetails, expiration);

        storeRefreshToken(customUserDetails.getUsername(), refreshToken);

        return refreshToken;
    }

    // CustomUserDetails 기반 토큰 생성
    private String tokenProvider(CustomUserDetails customUserDetails, Instant expiration) {
        Instant issuedAt = Instant.now();

        String authorities = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(customUserDetails.getUsername())
                .claim("role", authorities)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }
}
