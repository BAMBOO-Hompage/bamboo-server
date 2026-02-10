package SMU.BAMBOO.Hompage.domain.faq.repository;

import SMU.BAMBOO.Hompage.domain.faq.entity.Faq;

import java.util.List;
import java.util.Optional;

/**
 * FAQ Repository 인터페이스
 * 
 * 왜 인터페이스로 만드나?
 * 1. Service는 이 인터페이스만 알면 됨 (구현 방식 몰라도 됨)
 * 2. 테스트할 때 가짜 구현으로 교체 가능
 * 3. 나중에 구현 방식 바꿔도 Service 코드 수정 없음
 * 
 * 이런 패턴을 "의존성 역전(DIP)"이라고 함
 * - 구체적인 것(Impl)에 의존하지 않고
 * - 추상적인 것(Interface)에 의존
 */
public interface FaqRepository {

    /**
     * FAQ 저장 (생성/수정 모두 이걸로)
     * JPA는 ID가 없으면 INSERT, 있으면 UPDATE
     */
    Faq save(Faq faq);

    /**
     * ID로 조회 (없을 수도 있음)
     * Optional: null 대신 사용, 값이 있을 수도/없을 수도
     */
    Optional<Faq> findById(Long id);

    /**
     * ID로 조회 (없으면 에러 발생)
     * 확실히 있어야 하는 경우 사용
     */
    Faq getById(Long id);

    /**
     * 전체 조회 (표시 순서대로)
     */
    List<Faq> findAll();

    /**
     * 카테고리별 조회
     */
    List<Faq> findByCategory(String category);

    /**
     * 삭제
     */
    void delete(Long id);

    /**
     * 존재 여부 확인
     */
    boolean existsById(Long id);
}
