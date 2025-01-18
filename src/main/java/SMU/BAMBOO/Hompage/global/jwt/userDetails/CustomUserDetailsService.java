package SMU.BAMBOO.Hompage.global.jwt.userDetails;

import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // Username(학번)으로 CustomUserDetail 가져오기
    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {

        Optional<Member> findMember = memberRepository.findByStudentId(studentId);
        if (findMember.isPresent()) {
            Member member = findMember.get();
            return new CustomUserDetails(member.getStudentId(), member.getPw(), member.getRole());
        }
        throw new CustomException(ErrorCode.USER_NOT_EXIST);
    }
}
