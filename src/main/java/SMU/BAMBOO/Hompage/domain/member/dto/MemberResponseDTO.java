package SMU.BAMBOO.Hompage.domain.member.dto;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 관련 응답 DTO")
public class MemberResponseDTO {

    @Schema(description = "회원 로그인 응답 DTO")
    public record Login (
            Long memberId,
            String studentId,
            Role role
    ) {
        public static Login from(Member member) {
            return new Login(
                    member.getMemberId(),
                    member.getStudentId(),
                    member.getRole()
            );
        }
    }

    @Schema(description = "스터디 내 회원 정보 응답 DTO")
    public record MemberInStudy (
            Long memberId,
            String studentId,
            String name
    ) {
        public static MemberInStudy from(Member member) {
            return new MemberInStudy(
                    member.getMemberId(),
                    member.getStudentId(),
                    member.getName()
            );
        }
    }

    @Schema(description = "회원 정보 조회 응답 DTO")
    public record MemberInfo (
            Long memberId,
            String studentId,
            String email,
            String name,
            String major,
            String phone,
            Role role
    ) {
        public static MemberInfo from(Member member) {
            return new MemberInfo(
                    member.getMemberId(),
                    member.getStudentId(),
                    member.getEmail(),
                    member.getName(),
                    member.getMajor(),
                    member.getPhone(),
                    member.getRole()
            );
        }
    }

    @Schema(description = "마이 페이지 조회 응답 DTO")
    public record MyPage (
            Long memberId,
            String studentId,
            String email,
            String name,
            String major,
            String phone,
            Role role,
            String profileImageUrl
    ) {
        public static MyPage from(Member member) {
            return new MyPage(
                    member.getMemberId(),
                    member.getStudentId(),
                    member.getEmail(),
                    member.getName(),
                    member.getMajor(),
                    member.getPhone(),
                    member.getRole(),
                    member.getProfileImageUrl()
            );
        }
    }

    @Schema(description = "댓글 작성자 정보 DTO")
    public record CommentMemberInfo (
            String name,
            String profileImageUrl,
            String major
    ) {
        public static CommentMemberInfo from(Member member) {
            return new CommentMemberInfo(
                    member.getName(),
                    member.getProfileImageUrl(),
                    member.getMajor()
            );
        }

        public static CommentMemberInfo deletedUser() {
            return new CommentMemberInfo("알 수 없음", null, null);
        }
    }

}
