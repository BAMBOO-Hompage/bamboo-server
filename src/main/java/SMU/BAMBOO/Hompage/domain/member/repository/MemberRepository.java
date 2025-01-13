package SMU.BAMBOO.Hompage.domain.member.repository;

import SMU.BAMBOO.Hompage.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member getByStudentId(String studentId);

    Optional<Member> findById(long id);

    Optional<Member> findByStudentId(String studentId);

    Member save(Member member);

    List<Member> findAll();
}
