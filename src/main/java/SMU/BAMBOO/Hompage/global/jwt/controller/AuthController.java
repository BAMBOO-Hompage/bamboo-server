package SMU.BAMBOO.Hompage.global.jwt.controller;

import SMU.BAMBOO.Hompage.global.jwt.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "토큰 재발급 API", description = "토큰 재발급 API입니다.")
public class AuthController {

    private final AuthService authService;

    // 토큰 재발급 API
    @Operation(method = "POST", summary = "토큰 재발급", description = "Access Token과 Refresh Token을 헤더에 담아서 전송합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("Authorization") String accessToken,
                                     @RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(authService.reissueToken(accessToken, refreshToken));
    }
}