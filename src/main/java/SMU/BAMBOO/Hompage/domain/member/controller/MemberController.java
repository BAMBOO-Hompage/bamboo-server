package SMU.BAMBOO.Hompage.domain.member.controller;

import SMU.BAMBOO.Hompage.domain.member.dto.request.MemberLoginDto;
import SMU.BAMBOO.Hompage.domain.member.dto.request.MemberSignUpDto;
import SMU.BAMBOO.Hompage.domain.member.dto.response.LoginResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import SMU.BAMBOO.Hompage.domain.member.service.MemberService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "회원")
public class MemberController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder encoder;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입")
    public SuccessResponse<MemberResponse> signUp(@RequestBody MemberSignUpDto request) {
        MemberResponse response = memberService.signUp(request, encoder);
        return SuccessResponse.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public SuccessResponse<LoginResponse> login(@RequestBody MemberLoginDto request, HttpServletResponse response) {
        LoginResponse result = memberService.login(request, response);
        return SuccessResponse.ok(result);
    }
}
