package SMU.BAMBOO.Hompage.domain.member.repository;

import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByStudentId(String studentId);

    Optional<Member> findByEmail(String email);
}
