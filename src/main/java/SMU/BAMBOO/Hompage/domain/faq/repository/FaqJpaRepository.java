package SMU.BAMBOO.Hompage.domain.faq.repository;

import SMU.BAMBOO.Hompage.domain.faq.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * FAQ JPA Repository
 * 
 * JpaRepository<Entity타입, PK타입>을 상속받으면
 * 기본 CRUD 메서드가 자동으로 제공됨:
 * - save(entity): 저장/수정
 * - findById(id): ID로 조회
 * - findAll(): 전체 조회
 * - deleteById(id): 삭제
 * - count(): 개수
 * 등등...
 * 
 * 메서드 이름 규칙으로 쿼리 자동 생성도 가능:
 * - findByCategory(category) → WHERE category = ?
 * - findByQuestionContaining(keyword) → WHERE question LIKE '%keyword%'
 */
public interface FaqJpaRepository extends JpaRepository<Faq, Long> {

    /**
     * 카테고리로 FAQ 조회
     * 
     * 메서드 이름만 정의하면 Spring Data JPA가 자동으로 쿼리 생성!
     * findByCategory → SELECT * FROM faq WHERE category = ?
     */
    List<Faq> findByCategory(String category);

    /**
     * 표시 순서로 정렬해서 전체 조회
     * 
     * findAllByOrderBy{필드명}Asc → ORDER BY displayOrder ASC
     */
    List<Faq> findAllByOrderByDisplayOrderAsc();

    /**
     * 카테고리별 + 순서 정렬
     */
    List<Faq> findByCategoryOrderByDisplayOrderAsc(String category);
}
