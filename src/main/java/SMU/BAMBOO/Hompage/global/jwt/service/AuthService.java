package SMU.BAMBOO.Hompage.global.jwt.service;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.jwt.Token;
import SMU.BAMBOO.Hompage.global.jwt.repository.TokenRepository;
import SMU.BAMBOO.Hompage.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public String reissueToken(String accessToken, String refreshToken) {
        // Access Token에서 사용자 ID 추출 (예시로 Refresh Token 사용)
        String studentId = jwtUtil.getStudendId(refreshToken);

        // DB에 저장된 Refresh Token 확인
        Token refreshTokenByDB = tokenRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)
        );

        // Refresh Token 유효성 검사
        jwtUtil.validateToken(refreshToken);

        // Refresh Token 검증 및 새로운 Access Token 발급
        if (refreshTokenByDB.getToken().equals(refreshToken)) {
            return jwtUtil.reissueToken(refreshToken);
        } else {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }
    }
}

