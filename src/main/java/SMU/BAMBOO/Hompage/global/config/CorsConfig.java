package SMU.BAMBOO.Hompage.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("http://localhost:3000");
        config.addAllowedOriginPattern("http://localhost:8080");
        config.addAllowedOriginPattern("https://smu-bamboo.uk");
        config.addAllowedOriginPattern("https://www.smu-bamboo.uk");
        config.addAllowedOriginPattern("https://api.smu-bamboo.uk");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addExposedHeader("Authorization");
        config.addExposedHeader("refresh-token");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
