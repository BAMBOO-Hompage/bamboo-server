package SMU.BAMBOO.Hompage.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);   // 기본적으로 실행 대기 중인 스레드 개수
        executor.setMaxPoolSize(5);    // 동시에 동작하는 최대 스레드 개수
        executor.setQueueCapacity(20); // CorePool 의 크기는 넘어서는 경우 저장할 수 있는 큐의 최대 용량
        executor.setThreadNamePrefix("AsyncMailExecutor-");
        executor.initialize();
        return executor;
    }
}
