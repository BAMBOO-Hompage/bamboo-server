package SMU.BAMBOO.Hompage.domain.faq.repository;

import SMU.BAMBOO.Hompage.domain.faq.entity.Faq;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * FAQ Repository 구현체
 * 
 * @Repository: 스프링이 관리하는 Repository Bean으로 등록
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성 (DI용)
 * 
 * 이 클래스가 FaqRepository 인터페이스를 구현함
 * Service는 인터페이스만 알고, 이 구현체는 모름 (느슨한 결합)
 */
@Repository
@RequiredArgsConstructor
public class FaqRepositoryImpl implements FaqRepository {

    /**
     * JPA 기본 Repository (CRUD 기본 기능)
     */
    private final FaqJpaRepository faqJpaRepository;

    /**
     * QueryDSL용 (복잡한 쿼리 작성 시 사용)
     * 지금은 간단해서 안 쓰지만, 나중에 복잡한 검색 기능 추가할 때 사용
     */
    private final JPAQueryFactory queryFactory;

    /**
     * FAQ 저장
     * JPA save()는 ID가 없으면 INSERT, 있으면 UPDATE
     */
    @Override
    public Faq save(Faq faq) {
        return faqJpaRepository.save(faq);
    }

    /**
     * ID로 조회 (Optional 반환)
     * 값이 없을 수도 있는 경우 사용
     */
    @Override
    public Optional<Faq> findById(Long id) {
        return faqJpaRepository.findById(id);
    }

    /**
     * ID로 조회 (없으면 에러)
     * 반드시 있어야 하는 경우 사용
     * 
     * orElseThrow: Optional이 비어있으면 예외 발생
     */
    @Override
    public Faq getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FAQ_NOT_EXIST));
    }

    /**
     * 전체 조회 (표시 순서대로 정렬)
     */
    @Override
    public List<Faq> findAll() {
        return faqJpaRepository.findAllByOrderByDisplayOrderAsc();
    }

    /**
     * 카테고리별 조회 (표시 순서대로 정렬)
     */
    @Override
    public List<Faq> findByCategory(String category) {
        return faqJpaRepository.findByCategoryOrderByDisplayOrderAsc(category);
    }

    /**
     * 삭제
     */
    @Override
    public void delete(Long id) {
        faqJpaRepository.deleteById(id);
    }

    /**
     * 존재 여부 확인
     */
    @Override
    public boolean existsById(Long id) {
        return faqJpaRepository.existsById(id);
    }
}
