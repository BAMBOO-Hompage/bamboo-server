package SMU.BAMBOO.Hompage.domain.member.controller;

import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.dto.request.*;
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
import org.springframework.data.domain.Page;
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

    @GetMapping
    @Operation(summary = "회원 목록 조회 - 페이지네이션")
    public SuccessResponse<Page<MemberResponse>> getMembers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<MemberResponse> result = memberService.getMembers(page-1, size);
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

    @PatchMapping("/myPage/profileImage")
    @Operation(summary = "기본 프로필 이미지로 변경")
    public SuccessResponse<MyPageResponse> deleteProfileImage(@CurrentMember Member member) {
        MyPageResponse result = memberService.deleteProfileImage(member.getMemberId());
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

    @PatchMapping("/{memberId}/role")
    @Operation(summary = "권한 변경")
    public SuccessResponse<MemberResponse> updateRole(
            @CurrentMember Member member,
            @Valid @RequestBody UpdateRoleDto request) {
        MemberResponse result = memberService.updateRole(member.getMemberId(), request);
        return SuccessResponse.ok(result);
    }

    @PatchMapping("/{memberId}/role/test")
    @Operation(summary = "임원진 권한 없이 권한 변경 - 초기에 필요")
    public SuccessResponse<MemberResponse> testUpdateRole(
            @Valid @RequestBody TestUpdateRoleDto request) {
        MemberResponse result = memberService.testUpdateRole(request);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/deactivate")
    @Operation(summary = "회원 탈퇴 - 7일 후 자동 삭제")
    public SuccessResponse<String> deactivateMember(@CurrentMember Member member) {
        memberService.deactivateMember(member.getMemberId());
        return SuccessResponse.ok("회원 탈퇴 요청이 완료되었습니다. 7일 후 계정이 삭제됩니다.");
    }

    @PostMapping("/{memberId}/deactivate")
    @Operation(summary = "회원 삭제 - 7일 후 자동 삭제")
    public SuccessResponse<String> deleteMember(@PathVariable Long memberId) {
        memberService.deactivateMember(memberId);
        return SuccessResponse.ok("회원 비활성화에 성공했습니다.");
    }
}
