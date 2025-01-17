package SMU.BAMBOO.Hompage.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 인증이 필요하지 않은 URL 목록
    private final String[] allowedUrls = {
            "/",
            "/api/test",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/main-activities"
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // http basic 인증 방식 disable
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 인증이 필요하지 않은 URL 설정
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers(allowedUrls).permitAll()
                        .anyRequest().authenticated());
        // 세션 설정
        http.sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
