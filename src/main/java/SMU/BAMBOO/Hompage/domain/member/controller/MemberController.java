package SMU.BAMBOO.Hompage.domain.member.controller;

import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.dto.MemberRequestDTO;
import SMU.BAMBOO.Hompage.domain.member.dto.MemberResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.service.MemberService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import SMU.BAMBOO.Hompage.global.jwt.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "회원")
public class MemberController {

    private final MemberService.AuthenticationService authenticationService;
    private final MemberService.MemberInfoService memberInfoService;
    private final MemberService.AdminService adminService;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입")
    public SuccessResponse<MemberResponseDTO.MemberInfo> signUp(@RequestBody MemberRequestDTO.SignUp request) {
        MemberResponseDTO.MemberInfo result = authenticationService.signUp(request, encoder);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public SuccessResponse<MemberResponseDTO.Login> login(@RequestBody MemberRequestDTO.Login request, HttpServletResponse response) {
        MemberResponseDTO.Login result = authenticationService.login(request, response);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public SuccessResponse<String> logout(HttpServletRequest request) {
        String accessToken = jwtUtil.resolveAccessToken(request);
        String result = authenticationService.logout(accessToken);
        return SuccessResponse.ok(result);
    }

    @GetMapping
    @Operation(summary = "회원 목록 조회 - 페이지네이션")
    public SuccessResponse<Page<MemberResponseDTO.MemberInfo>> getMembers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<MemberResponseDTO.MemberInfo> result = memberInfoService.getMembers(page-1, size);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/myPage")
    @Operation(summary = "마이페이지")
    public SuccessResponse<MemberResponseDTO.MyPage> myPage(@CurrentMember Member member) {
        Member my = memberInfoService.getMember(member.getStudentId());
        return SuccessResponse.ok(MemberResponseDTO.MyPage.from(my));
    }

    @PatchMapping("/myPage")
    @Operation(summary = "프로필 변경")
    public SuccessResponse<MemberResponseDTO.MyPage> updateProfile(
            @CurrentMember Member member,
            @ModelAttribute MemberRequestDTO.UpdateProfile request
    ) {
        MemberResponseDTO.MyPage result = memberInfoService.updateProfile(member.getMemberId(), request);
        return SuccessResponse.ok(result);
    }

    @PatchMapping("/myPage/profileImage")
    @Operation(summary = "기본 프로필 이미지로 변경")
    public SuccessResponse<MemberResponseDTO.MyPage> deleteProfileImage(@CurrentMember Member member) {
        MemberResponseDTO.MyPage result = memberInfoService.deleteProfileImage(member.getMemberId());
        return SuccessResponse.ok(result);
    }

    @PatchMapping("/myPage/password")
    @Operation(summary = "비밀번호 변경")
    public SuccessResponse<String> updatePassword(
            @CurrentMember Member member,
            @RequestBody MemberRequestDTO.UpdatePw request) {
        memberInfoService.updatePw(member.getMemberId(), request);
        return SuccessResponse.ok("비밀번호를 변경했습니다.");
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 초기화 - 비로그인")
    public SuccessResponse<String> resetPassword(
            @RequestBody MemberRequestDTO.ResetPw request) {
        memberInfoService.resetPw(request);
        return SuccessResponse.ok("비밀번호를 초기화했습니다.");
    }

    @PatchMapping("/{memberId}/role")
    @Operation(summary = "권한 변경")
    public SuccessResponse<MemberResponseDTO.MemberInfo> updateRole(
            @CurrentMember Member member,
            @RequestBody MemberRequestDTO.UpdateRole request) {
        MemberResponseDTO.MemberInfo result = adminService.updateRole(member.getMemberId(), request);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/deactivate")
    @Operation(summary = "회원 탈퇴 - 7일 후 자동 삭제")
    public SuccessResponse<String> deactivateMember(@CurrentMember Member member) {
        adminService.deactivateMember(member.getMemberId());
        return SuccessResponse.ok("회원 탈퇴 요청이 완료되었습니다. 7일 후 계정이 삭제됩니다.");
    }

    @PostMapping("/{memberId}/deactivate")
    @Operation(summary = "회원 삭제 - 7일 후 자동 삭제")
    public SuccessResponse<String> deleteMember(@PathVariable Long memberId) {
        adminService.deactivateMember(memberId);
        return SuccessResponse.ok("회원 비활성화에 성공했습니다.");
    }

    @GetMapping("/exists")
    @Operation(summary = "학번으로 회원 존재 여부 확인")
    public SuccessResponse<Boolean> checkStudentIdExists(@RequestParam("studentId") String studentId) {
        boolean exists = memberInfoService.existsByStudentId(studentId);
        return SuccessResponse.ok(exists);
    }

}
