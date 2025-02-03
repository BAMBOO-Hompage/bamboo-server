package SMU.BAMBOO.Hompage.domain.libraryPost.repository;

import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import SMU.BAMBOO.Hompage.domain.libraryPost.entity.QLibraryPost;
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
        Long totalCount = Optional.ofNullable(queryFactory
                .select(libraryPost.count())
                .from(libraryPost)
                .fetchOne()).orElse(0L);

        // 데이터 조회
        List<LibraryPost> libraryPosts = queryFactory
                .selectFrom(libraryPost)
                .orderBy(libraryPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(libraryPosts, pageable, totalCount);
    }
}
