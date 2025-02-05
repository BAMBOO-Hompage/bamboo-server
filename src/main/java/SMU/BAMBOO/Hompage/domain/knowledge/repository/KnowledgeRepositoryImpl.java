package SMU.BAMBOO.Hompage.domain.knowledge.repository;

import SMU.BAMBOO.Hompage.domain.enums.KnowledgeType;
import SMU.BAMBOO.Hompage.domain.knowledge.entity.Knowledge;
import SMU.BAMBOO.Hompage.domain.knowledge.entity.QKnowledge;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Wildcard;
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
public class KnowledgeRepositoryImpl implements KnowledgeRepository {

    private final KnowledgeJpaRepository knowledgeJpaRepository;
    private final JPAQueryFactory queryFactory;

    public Knowledge getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.KNOWLEDGE_NOT_EXIST));
    }
    @Override
    public Optional<Knowledge> findById(Long id) {
        return knowledgeJpaRepository.findById(id);
    }

    @Override
    public Knowledge save(Knowledge knowledge) {
        return knowledgeJpaRepository.save(knowledge);
    }

    @Override
    public void deleteById(Long id) {
        knowledgeJpaRepository.deleteById(id);
    }

    @Override
    public Page<Knowledge> findAllByType(KnowledgeType type, Pageable pageable) {
        QKnowledge knowledge = QKnowledge.knowledge;

        // 전체 개수 조회
        long total = Optional.ofNullable(
                queryFactory
                        .select(Wildcard.count)
                        .from(knowledge)
                        .where(knowledge.type.eq(type))
                        .fetchOne()
        ).orElse(0L);

        // 데이터 조회
        List<Knowledge> results = queryFactory
                .selectFrom(knowledge)
                .where(knowledge.type.eq(type))
                .orderBy(knowledge.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Knowledge> findAllByTitle(String title, Pageable pageable) {
        QKnowledge knowledge = QKnowledge.knowledge;

        // 전체 개수 조회
        long total = Optional.ofNullable(
                queryFactory
                        .select(Wildcard.count)
                        .from(knowledge)
                        .where(knowledge.title.containsIgnoreCase(title))
                        .fetchOne()
        ).orElse(0L);

        // 데이터 조회
        List<Knowledge> results = queryFactory
                .selectFrom(knowledge)
                .where(knowledge.title.containsIgnoreCase(title))
                .orderBy(knowledge.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Knowledge> findByTypeAndTitle(KnowledgeType type, String keyword, Pageable pageable) {
        QKnowledge knowledge = QKnowledge.knowledge;
        BooleanBuilder whereClause = new BooleanBuilder();

        // 특정 type 필터링
        if (type != null) {
            whereClause.and(knowledge.type.eq(type));
        }
        // title 검색
        if (keyword != null && !keyword.isEmpty()) {
            whereClause.and(knowledge.title.containsIgnoreCase(keyword));
        }

        long total = Optional.ofNullable(
                queryFactory
                        .select(Wildcard.count)
                        .from(knowledge)
                        .where(whereClause)
                        .fetchOne()
        ).orElse(0L);

        List<Knowledge> results = queryFactory
                .selectFrom(knowledge)
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(knowledge.createdAt.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Knowledge> findAll(Pageable pageable) {
        QKnowledge knowledge = QKnowledge.knowledge;

        // 전체 개수 조회
        long total = Optional.ofNullable(
                queryFactory
                        .select(Wildcard.count)
                        .from(knowledge)
                        .fetchOne()
        ).orElse(0L);

        // 데이터 조회
        List<Knowledge> results = queryFactory
                .selectFrom(knowledge)
                .orderBy(knowledge.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }
}
