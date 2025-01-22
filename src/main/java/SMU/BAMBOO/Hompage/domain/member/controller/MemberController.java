package SMU.BAMBOO.Hompage.domain.member.controller;

import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.dto.request.MemberLoginDto;
import SMU.BAMBOO.Hompage.domain.member.dto.request.MemberSignUpDto;
import SMU.BAMBOO.Hompage.domain.member.dto.request.UpdateProfileDto;
import SMU.BAMBOO.Hompage.domain.member.dto.request.UpdatePwDto;
import SMU.BAMBOO.Hompage.domain.member.dto.response.LoginResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MyPageResponse;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.service.MemberService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import SMU.BAMBOO.Hompage.global.jwt.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    private final JwtUtil jwtUtil;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입")
    public SuccessResponse<MemberResponse> signUp(@RequestBody MemberSignUpDto request) {
        MemberResponse result = memberService.signUp(request, encoder);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public SuccessResponse<LoginResponse> login(@RequestBody MemberLoginDto request, HttpServletResponse response) {
        LoginResponse result = memberService.login(request, response);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public SuccessResponse<String> logout(HttpServletRequest request) {
        String accessToken = jwtUtil.resolveAccessToken(request);
        String result = memberService.logout(accessToken);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/myPage")
    @Operation(summary = "마이페이지")
    public SuccessResponse<MyPageResponse> myPage(@CurrentMember Member member) {
        Member my = memberService.getMember(member.getStudentId());
        return SuccessResponse.ok(MyPageResponse.from(my));
    }

    @PatchMapping("/myPage")
    @Operation(summary = "프로필 변경")
    public SuccessResponse<MyPageResponse> updateProfile(
            @CurrentMember Member member,
            @Valid @ModelAttribute UpdateProfileDto request
    ) {
        MyPageResponse result = memberService.updateProfile(member.getMemberId(), request);
        return SuccessResponse.ok(result);
    }

    @PatchMapping("/myPage/password")
    @Operation(summary = "비밀번호 변경")
    public SuccessResponse<String> updatePassword(
            @CurrentMember Member member,
            @Valid @RequestBody UpdatePwDto request) {
        memberService.updatePw(member.getMemberId(), request);
        return SuccessResponse.ok("비밀번호를 변경했습니다.");
    }

}
