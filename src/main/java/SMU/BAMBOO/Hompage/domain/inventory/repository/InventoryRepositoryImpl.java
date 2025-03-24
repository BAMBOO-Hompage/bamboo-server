package SMU.BAMBOO.Hompage.domain.inventory.repository;

import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.inventory.entity.QInventory;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.entity.QMember;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static SMU.BAMBOO.Hompage.domain.award.entity.QAward.award;
import static SMU.BAMBOO.Hompage.domain.cohort.entity.QCohort.cohort;
import static SMU.BAMBOO.Hompage.domain.inventory.entity.QInventory.inventory;
import static SMU.BAMBOO.Hompage.domain.mapping.memberStudy.entity.QMemberStudy.memberStudy;
import static SMU.BAMBOO.Hompage.domain.member.entity.QMember.member;
import static SMU.BAMBOO.Hompage.domain.study.entity.QStudy.study;
import static SMU.BAMBOO.Hompage.domain.subject.entity.QSubject.subject;

@Repository
@RequiredArgsConstructor
public class InventoryRepositoryImpl implements InventoryRepository {

    private final InventoryJpaRepository inventoryJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Inventory getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVENTORY_NOT_EXIST));
    }

    @Override
    public Optional<Inventory> findById(Long id) {
        return inventoryJpaRepository.findById(id);
    }

    @Override
    public Page<Inventory> findByStudy(Long studyId, Pageable pageable) {
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
                .leftJoin(inventory.member, member).fetchJoin()
                .leftJoin(inventory.study, study).fetchJoin()
                .leftJoin(inventory.award, award).fetchJoin()
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
        QMember inventoryMember = new QMember("inventoryMember");
        QMember studyMember = new QMember("studyMember");

        // 전체 개수 조회 (NPE 방지를 위해 Optional.ofNullable 로 기본값 설정)
        Long totalCount = Optional.ofNullable(queryFactory
                .select(inventory.count())
                .from(inventory)
                .fetchOne()).orElse(0L);

        // 데이터 조회
        List<Inventory> inventories = queryFactory
                .selectFrom(inventory)
                .leftJoin(inventory.member, inventoryMember).fetchJoin()
                .leftJoin(inventory.study, study).fetchJoin()
                .leftJoin(study.subject, subject).fetchJoin()
                .leftJoin(study.cohort, cohort).fetchJoin()
                .leftJoin(study.memberStudies, memberStudy).fetchJoin()
                .leftJoin(memberStudy.member, studyMember).fetchJoin()
                .leftJoin(inventory.award, award).fetchJoin()
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

    @Override
    public Optional<Inventory> findByStudyIdAndWeekAndMemberId(Long studyId, int week, Long memberId) {
        Inventory result = queryFactory
                .selectFrom(inventory)
                .where(
                        inventory.study.studyId.eq(studyId)
                                .and(inventory.week.eq(week))
                                .and(inventory.member.memberId.eq(memberId))
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public void resetWeeklyBest(Long studyId, int week) {
        QInventory inventory = QInventory.inventory;

        queryFactory.update(inventory)
                .set(inventory.isWeeklyBest, false)
                .where(
                        inventory.study.studyId.eq(studyId)
                                .and(inventory.week.eq(week))
                                .and(inventory.isWeeklyBest.isTrue())
                )
                .execute();
    }

    @Override
    public Optional<Inventory> findWeeklyBestByStudyIdAndWeek(Long studyId, int week) {
        Inventory result = queryFactory
                .selectFrom(inventory)
                .where(
                        inventory.study.studyId.eq(studyId)
                                .and(inventory.week.eq(week))
                                .and(inventory.isWeeklyBest.isTrue())
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

}
