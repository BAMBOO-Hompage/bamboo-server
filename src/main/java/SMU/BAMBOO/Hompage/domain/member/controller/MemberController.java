package SMU.BAMBOO.Hompage.domain.member.controller;

import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.dto.request.MemberLoginDto;
import SMU.BAMBOO.Hompage.domain.member.dto.request.MemberSignUpDto;
import SMU.BAMBOO.Hompage.domain.member.dto.response.LoginResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.service.MemberService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "회원")
public class MemberController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder encoder;

    @GetMapping("/member")
    public SuccessResponse<?> getMember(@CurrentMember Member member) {
        if (member == null) {
            return SuccessResponse.ok("실패");
        }

        // Member 정보 반환
        MemberResponse response = MemberResponse.from(member);
        return SuccessResponse.ok(response);
    }

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
