package SMU.BAMBOO.Hompage.domain.notice.repository;

import SMU.BAMBOO.Hompage.domain.enums.NoticeType;
import SMU.BAMBOO.Hompage.domain.member.entity.QMember;
import SMU.BAMBOO.Hompage.domain.notice.entity.Notice;
import SMU.BAMBOO.Hompage.domain.notice.entity.QNotice;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepository {

    private final NoticeJpaRepository noticeJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Notice save(Notice notice){
        return noticeJpaRepository.save(notice);
    }

    @Override
    public Optional<Notice> findById(Long id) {
        QMember member = QMember.member;
        QNotice notice = QNotice.notice;

        Notice result = queryFactory
                .selectFrom(notice)
                .leftJoin(notice.member, member).fetchJoin()
                .where(notice.noticeId.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public void deleteById(Long id){
        noticeJpaRepository.deleteById(id);
    }

    @Override
    public Page<Notice> findByType(NoticeType type, Pageable pageable) {
        QMember member = QMember.member;
        QNotice notice = QNotice.notice;

        List<Notice> result = queryFactory
                .selectFrom(notice)
                .leftJoin(notice.member, member).fetchJoin()
                .where(notice.type.eq(type))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notice.createdAt.desc())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                .select(notice.count())
                .from(notice)
                .where(notice.type.eq(type))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<Notice> findAll(Pageable pageable) {
        QMember member = QMember.member;
        QNotice notice = QNotice.notice;

        List<Notice> result = queryFactory
                .selectFrom(notice)
                .leftJoin(notice.member, member).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notice.createdAt.desc())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                .select(notice.count())
                .from(notice)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(result, pageable, total);
    }

}
