package SMU.BAMBOO.Hompage;

import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "테스트")
public class TestController {

    @GetMapping
    @Operation(summary = "테스트 API")
    public SuccessResponse<String> DoTest() {
        String result = "Success Test";
        return SuccessResponse.ok(result);
    }
}
