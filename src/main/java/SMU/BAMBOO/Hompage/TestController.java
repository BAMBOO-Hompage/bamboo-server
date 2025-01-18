package SMU.BAMBOO.Hompage;

import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "테스트")
public class TestController {

    @GetMapping("/test")
    @Operation(summary = "인증X 테스트 API")
    public SuccessResponse<String> DoTest() {
        String result = "Success Test";
        return SuccessResponse.ok(result);
    }

    @GetMapping("/test-fail")
    @Operation(summary = "인증 필요한 테스트 API")
    public SuccessResponse<String> DoFailTest() {
        String result = "Test";
        return SuccessResponse.ok(result);
    }
}
