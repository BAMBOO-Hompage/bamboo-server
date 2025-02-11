package SMU.BAMBOO.Hompage.domain.inventory.repository;

import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.inventory.entity.QInventory;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.study.entity.QStudy;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
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
public class InventoryRepositoryImpl implements InventoryRepository {

    private final InventoryJpaRepository inventoryJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Inventory> findById(Long id) {
        return inventoryJpaRepository.findById(id);
    }

    @Override
    public Page<Inventory> findByStudy(Long studyId, Pageable pageable) {
        QInventory inventory = QInventory.inventory;
        QStudy study = QStudy.study;

        // 전체 개수 조회 (NPE 방지 위해 Optional 사용)
        Long totalCount = Optional.ofNullable(
                queryFactory.select(inventory.count())
                        .from(inventory)
                        .where(inventory.study.studyId.eq(studyId))
                        .fetchOne()
        ).orElse(0L);

        // 데이터 조회
        List<Inventory> inventories = queryFactory
                .selectFrom(inventory)
                .where(inventory.study.studyId.eq(studyId))
                .orderBy(inventory.week.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(inventories, pageable, totalCount);
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryJpaRepository.findAll();
    }

    @Override
    public Page<Inventory> findByPage(Pageable pageable) {
        QInventory inventory = QInventory.inventory;

        // 전체 개수 조회 (NPE 방지를 위해 Optional.ofNullable 로 기본값 설정)
        Long totalCount = Optional.ofNullable(queryFactory
                .select(inventory.count())
                .from(inventory)
                .fetchOne()).orElse(0L);

        // 데이터 조회
        List<Inventory> inventories = queryFactory
                .selectFrom(inventory)
                .orderBy(inventory.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(inventories, pageable, totalCount);
    }

    @Override
    public Inventory save(Inventory inventory) {
        return inventoryJpaRepository.save(inventory);
    }

    @Override
    public void deleteById(Long id) {
        inventoryJpaRepository.deleteById(id);
    }

    @Override
    public Boolean existsByMemberAndStudyAndWeek(Member member, Study study, int week) {
        return inventoryJpaRepository.existsByMemberAndStudyAndWeek(member, study, week);
    }
}
