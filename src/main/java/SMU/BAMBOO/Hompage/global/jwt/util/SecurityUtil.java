package SMU.BAMBOO.Hompage.global.jwt.util;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    /**
     * 현재 로그인 중인 회원의 학번을 조회
     */
    public static String getCurrentStudentId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // studentId 반환
        } else if (principal instanceof String) {
            return (String) principal; // 일반 문자열인 경우
        }

        throw new CustomException(ErrorCode.USER_NOT_VALID);
    }

    /**
     * 현재 로그인 중인 회원의 등급을 조회
     */
    public static boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }
}
