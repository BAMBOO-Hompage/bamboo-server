package SMU.BAMBOO.Hompage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BambooHompageApplication {

	public static void main(String[] args) {
		SpringApplication.run(BambooHompageApplication.class, args);
	}

}
