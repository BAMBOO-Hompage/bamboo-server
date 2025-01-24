package SMU.BAMBOO.Hompage.domain.member.repository;

import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberRepository {

    Member getByStudentId(String studentId);

    Optional<Member> findById(long id);

    Optional<Member> findByStudentId(String studentId);

    Optional<Member> findByEmail(String email);

    Member save(Member member);

    Page<Member> findAll(Pageable pageable);

    Page<Member> findAllWithCustomSort(Pageable pageable);
}
