package SMU.BAMBOO.Hompage.global.controller;

import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "서버 상태 체크를 위한 API")
public class HealthCheckController {
    @GetMapping
    public SuccessResponse<String> checkHealth() {
        return SuccessResponse.ok("OK");
    }
}
