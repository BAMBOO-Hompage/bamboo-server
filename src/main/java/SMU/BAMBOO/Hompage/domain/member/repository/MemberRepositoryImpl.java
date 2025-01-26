package SMU.BAMBOO.Hompage.domain.member.repository;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.entity.QMember;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Member getByStudentId(String studentId) {
        return findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
    }

    @Override
    public Optional<Member> findById(long id) {
        return memberJpaRepository.findById(id);
    }

    @Override
    public Optional<Member> findByStudentId(String studentId) {
        return memberJpaRepository.findByStudentId(studentId);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public Page<Member> findAll(Pageable pageable) {
        return memberJpaRepository.findAll(pageable);
    }

    @Override
    public Page<Member> findAllSortByRole(Pageable pageable) {
        QMember member = QMember.member;

        // Role 에 변경이 없을 것이라 판단
        List<Member> content = queryFactory.selectFrom(member)
                .orderBy(new CaseBuilder()
                        .when(member.role.eq(Role.ROLE_USER)).then(4)
                        .when(member.role.eq(Role.ROLE_MEMBER)).then(3)
                        .when(member.role.eq(Role.ROLE_ADMIN)).then(2)
                        .when(member.role.eq(Role.ROLE_OPS)).then(1)
                        .otherwise(0) // 기본값
                        .asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(member.count().coalesce(0L)) // null이면 0L 반환
                .from(member)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
