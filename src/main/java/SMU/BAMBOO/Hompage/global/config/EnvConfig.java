package SMU.BAMBOO.Hompage.global.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    static {
        Dotenv dotenv = Dotenv.load(); // .env 파일 로드
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue()) // 환경 변수 설정
        );
    }
}
