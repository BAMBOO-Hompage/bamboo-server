package SMU.BAMBOO.Hompage.global.jwt.filter;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.jwt.userDetails.CustomUserDetails;
import SMU.BAMBOO.Hompage.global.jwt.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // Spring Security Filter Chain 내부에서 동작할 로직
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // Request 에서 access token 추출
            String accessToken = jwtUtil.resolveAccessToken(request);

            // accessToken 없이 접근할 경우 필터를 건너뜀
            if (accessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            authenticateAccessToken(accessToken);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            handleCustomException(response, new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRED));
        }
    }

    private void authenticateAccessToken(String accessToken) {
        jwtUtil.validateToken(accessToken);

        // CustomUserDetails 객체 생성
        String roleString = jwtUtil.getRole(accessToken);
        Role role = Role.valueOf(roleString);

        CustomUserDetails userDetails = new CustomUserDetails(
                jwtUtil.getStudentId(accessToken),
                null,
                role
        );

        // Spring Security 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        // SecurityContextHolder에 현재 인증 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void handleCustomException(HttpServletResponse response, CustomException e) throws IOException {
        response.setStatus(e.getErrorCode().getStatus().value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // JSON 응답 데이터 생성
        String jsonResponse = String.format(
                "{\"code\": \"%s\", \"message\": \"%s\", \"result\": null, \"success\": false}",
                e.getErrorCode().name(),
                e.getErrorCode().getMessage()
        );

        response.getWriter().write(jsonResponse);
    }
}

