package SMU.BAMBOO.Hompage.global.config;

import SMU.BAMBOO.Hompage.global.exception.CustomAuthenticationEntryPoint;
import SMU.BAMBOO.Hompage.global.jwt.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    // 인증이 필요하지 않은 URL 목록
    private final String[] allowedUrls = {
            "/",
            "/api/test",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/members/login",
            "/api/members/sign-up",
            "/api/emails/**",
            "/auth/reissue",
            "/api/main-activities/year"
    };

    // 운영진 이상의 권한 필요 (ADMIN, OPS)
    private final String[] adminUrls = {
            "/api/subjects/**",
            "/api/studies/**"
    };

    // 회원 이상의 권한 필요 (MEMBER, ADMIN, OPS)
    private final String[] memberUrls = {
            "/api/inventories/**",
            "/api/knowledges/**"
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정 활성화
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // csrf 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // REST API 방식 로그인을 사용하기 때문에 Form 로그인 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);

        // http basic 인증 방식 disable
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 세션 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 경로별 인가 설정
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(allowedUrls).permitAll()
                .requestMatchers(HttpMethod.GET, adminUrls).permitAll()
                .requestMatchers(HttpMethod.POST, adminUrls).hasAnyAuthority("ROLE_ADMIN", "ROLE_OPS")
                .requestMatchers(HttpMethod.PUT, adminUrls).hasAnyAuthority("ROLE_ADMIN", "ROLE_OPS")
                .requestMatchers(HttpMethod.PATCH, adminUrls).hasAnyAuthority("ROLE_ADMIN", "ROLE_OPS")
                .requestMatchers(HttpMethod.DELETE, adminUrls).hasAnyAuthority("ROLE_ADMIN", "ROLE_OPS")
                .requestMatchers(HttpMethod.GET, memberUrls).permitAll()
                .requestMatchers(HttpMethod.POST, memberUrls).hasAnyAuthority("ROLE_MEMBER", "ROLE_ADMIN", "ROLE_OPS")
                .requestMatchers(HttpMethod.PUT, memberUrls).hasAnyAuthority("ROLE_MEMBER", "ROLE_ADMIN", "ROLE_OPS")
                .requestMatchers(HttpMethod.PATCH, memberUrls).hasAnyAuthority("ROLE_MEMBER", "ROLE_ADMIN", "ROLE_OPS")
                .requestMatchers(HttpMethod.DELETE, memberUrls).hasAnyAuthority("ROLE_MEMBER", "ROLE_ADMIN", "ROLE_OPS")
                .anyRequest().authenticated());

        // 예외 처리 설정
        http.exceptionHandling(e -> e
                .authenticationEntryPoint(customAuthenticationEntryPoint));

        // JWT 필터 추가
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000"); // 허용할 도메인
        config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용 (GET, POST 등)
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addExposedHeader("Authorization");
        config.addExposedHeader("refresh-token");
        config.setAllowCredentials(true); // 쿠키 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 설정
        // source.registerCorsConfiguration("/api/**", config);

        return source;
    }
}
