package SMU.BAMBOO.Hompage.global.jwt.service;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;

    public String reissueToken(String accessToken, String refreshToken) {
        // Access Token 에서 사용자 ID 추출 (예시로 Refresh Token 사용)
        String studentId = jwtUtil.getStudentId(refreshToken);

        // Redis 에서 Refresh Token 확인
        String storedRefreshToken = jwtUtil.getRefreshTokenFromRedis(studentId);
        if (storedRefreshToken == null) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        // Refresh Token 유효성 검사
        jwtUtil.validateToken(refreshToken);

        // Refresh Token 검증 및 새로운 Access Token 발급
        if (storedRefreshToken.equals(refreshToken)) {
            return jwtUtil.reissueToken(refreshToken);
        } else {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }
    }
}

