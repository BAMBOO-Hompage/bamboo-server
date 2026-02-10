package SMU.BAMBOO.Hompage.global.config;

import SMU.BAMBOO.Hompage.global.exception.CustomAuthenticationEntryPoint;
import SMU.BAMBOO.Hompage.global.jwt.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
            "/api/members/password",
            "/api/members/exists",
            "/api/emails/**",
            "/auth/reissue",
            "/api/main-activities/year",
            "/health",
            "/actuator"
    };

    // FAQ 조회만 로그인 없이 허용 (다른 규칙보다 위에서 매칭되도록 별도 정의)
    private final String[] faqGetUrls = {
            "/api/faqs",
            "/api/faqs/**"
    };

    // 임원진 이상의 권한 필요 (OPS)
    private final String[] opsUrls = {
            "/api/cohorts/**"
    };

    // 운영진 이상의 권한 필요 (ADMIN, OPS) - FAQ는 GET만 공개, 쓰기는 아래 faqWriteUrls에서 처리
    private final String[] adminUrls = {
            "/api/awards/**",
            "/api/main-activities/**",
            "/api/notices/**",
            "/api/studies/**",
            "/api/subjects/**",
            "/api/tags/**",
    };

    // FAQ 생성/수정/삭제 (ADMIN, OPS만)
    private final String[] faqWriteUrls = {
            "/api/faqs",
            "/api/faqs/**"
    };

    // 회원 이상의 권한 필요 (MEMBER, ADMIN, OPS)
    private final String[] memberUrls = {
            "/api/inventories/**",
            "/api/knowledges/**",
            "/api/knowledge-comments/**",
            "/api/library-posts/**"
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

        // CORS 설정
        http.cors(Customizer.withDefaults());

        // csrf 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // REST API 방식 로그인을 사용하기 때문에 Form 로그인 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);

        // http basic 인증 방식 disable
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 세션 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 경로별 인가 설정 (위에 있을수록 우선 매칭)
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(allowedUrls).permitAll()
                // FAQ 조회: 로그인 없이 허용 (다른 /api/** 규칙보다 먼저 적용)
                .requestMatchers(HttpMethod.GET, faqGetUrls).permitAll()
                .requestMatchers(HttpMethod.GET, opsUrls).permitAll()
                .requestMatchers(HttpMethod.POST, opsUrls).hasAnyAuthority("ROLE_OPS")
                .requestMatchers(HttpMethod.DELETE, opsUrls).hasAnyAuthority("ROLE_OPS")
                .requestMatchers(HttpMethod.GET, adminUrls).permitAll()
                .requestMatchers(HttpMethod.POST, adminUrls).hasAnyAuthority("ROLE_ADMIN", "ROLE_OPS")
                .requestMatchers(HttpMethod.PUT, adminUrls).hasAnyAuthority("ROLE_ADMIN", "ROLE_OPS")
                .requestMatchers(HttpMethod.PATCH, adminUrls).hasAnyAuthority("ROLE_ADMIN", "ROLE_OPS")
                .requestMatchers(HttpMethod.DELETE, adminUrls).hasAnyAuthority("ROLE_ADMIN", "ROLE_OPS")
                // FAQ 생성/수정/삭제: ADMIN, OPS만
                .requestMatchers(HttpMethod.POST, faqWriteUrls).hasAnyAuthority("ROLE_ADMIN", "ROLE_OPS")
                .requestMatchers(HttpMethod.PATCH, faqWriteUrls).hasAnyAuthority("ROLE_ADMIN", "ROLE_OPS")
                .requestMatchers(HttpMethod.DELETE, faqWriteUrls).hasAnyAuthority("ROLE_ADMIN", "ROLE_OPS")
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

}
