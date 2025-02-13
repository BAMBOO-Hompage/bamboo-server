package SMU.BAMBOO.Hompage.domain.libraryPost.repository;

import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import SMU.BAMBOO.Hompage.domain.libraryPost.entity.QLibraryPost;
import SMU.BAMBOO.Hompage.domain.mapping.libraryPostTag.QLibraryPostTag;
import SMU.BAMBOO.Hompage.domain.tag.entity.QTag;
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
public class LibraryPostRepositoryImpl implements LibraryPostRepository {

    private final LibraryPostJpaRepository libraryPostJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<LibraryPost> findById(Long id) {
        return libraryPostJpaRepository.findById(id);
    }

    @Override
    public Optional<LibraryPost> findByPaperName(String name) {
        return libraryPostJpaRepository.findByPaperName(name);
    }

    @Override
    public LibraryPost save(LibraryPost libraryPost) {
        return libraryPostJpaRepository.save(libraryPost);
    }

    @Override
    public void deleteById(Long id) {
        libraryPostJpaRepository.deleteById(id);
    }

    @Override
    public Page<LibraryPost> findByPage(Pageable pageable) {
        QLibraryPost libraryPost = QLibraryPost.libraryPost;

        // 전체 개수 조회 (NPE 방지를 위해 Optional.ofNullable 로 기본값 설정)
        long totalCount = Optional.ofNullable(
                queryFactory
                        .select(Wildcard.count)
                        .from(libraryPost)
                        .fetchOne()
            ).orElse(0L);

        // 데이터 조회
        List<LibraryPost> libraryPosts = queryFactory
                .selectFrom(libraryPost)
                .orderBy(libraryPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(libraryPosts, pageable, totalCount);
    }

    @Override
    public Page<LibraryPost> findByPaperName(String paperName, Pageable pageable) {
        QLibraryPost libraryPost = QLibraryPost.libraryPost;

        long total = Optional.ofNullable(
                queryFactory
                        .select(Wildcard.count)  // COUNT(*)의 역할
                        .from(libraryPost)
                        .where(libraryPost.paperName.containsIgnoreCase(paperName))
                        .fetchOne()
                ).orElse(0L);

        List<LibraryPost> results = queryFactory
                .selectFrom(libraryPost)
                .where(libraryPost.paperName.containsIgnoreCase(paperName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(libraryPost.createdAt.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<LibraryPost> findByYear(String year, Pageable pageable) {
        QLibraryPost libraryPost = QLibraryPost.libraryPost;

        long total = Optional.ofNullable(
                queryFactory
                        .select(Wildcard.count)
                        .from(libraryPost)
                        .where(libraryPost.year.eq(year))
                        .fetchOne()
            ).orElse(0L);


        List<LibraryPost> results = queryFactory
                .selectFrom(libraryPost)
                .where(libraryPost.year.eq(year))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(libraryPost.createdAt.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<LibraryPost> findByTag(String tag, Pageable pageable) {
        QLibraryPost libraryPost = QLibraryPost.libraryPost;
        QLibraryPostTag libraryPostTag = QLibraryPostTag.libraryPostTag;
        QTag tagEntity = QTag.tag;

        long total = Optional.ofNullable(
                queryFactory
                        .select(Wildcard.count)
                        .from(libraryPost)
                        .join(libraryPostTag).on(libraryPostTag.libraryPost.eq(libraryPost))
                        .join(tagEntity).on(tagEntity.eq(libraryPostTag.tag))
                        .where(tagEntity.name.eq(tag))
                        .fetchOne()
            ).orElse(0L);

        List<LibraryPost> result = queryFactory
                .selectFrom(libraryPost)
                .join(libraryPostTag).on(libraryPostTag.libraryPost.eq(libraryPost))
                .join(tagEntity).on(tagEntity.eq(libraryPostTag.tag))
                .where(tagEntity.name.eq(tag))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(libraryPost.createdAt.desc())
                .fetch();

        return new PageImpl<>(result, pageable, total);
    }
}
