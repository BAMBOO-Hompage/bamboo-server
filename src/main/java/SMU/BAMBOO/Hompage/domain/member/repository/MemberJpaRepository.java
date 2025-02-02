package SMU.BAMBOO.Hompage.domain.member.repository;

import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByStudentId(String studentId);

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.studentId IN :studentIds")
    List<Member> findAllByStudentId(@Param("studentIds") List<String> studentIds);
}
