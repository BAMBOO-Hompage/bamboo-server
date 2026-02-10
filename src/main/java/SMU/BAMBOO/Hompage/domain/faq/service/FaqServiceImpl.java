package SMU.BAMBOO.Hompage.domain.faq.service;

import SMU.BAMBOO.Hompage.domain.faq.dto.FaqRequestDTO;
import SMU.BAMBOO.Hompage.domain.faq.dto.FaqResponseDTO;
import SMU.BAMBOO.Hompage.domain.faq.entity.Faq;
import SMU.BAMBOO.Hompage.domain.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * FAQ Service 구현체
 * 
 * @Service: 스프링이 관리하는 Service Bean으로 등록
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성
 * @Transactional(readOnly = true): 기본적으로 읽기 전용 트랜잭션
 *   - 읽기 전용이면 JPA가 더티체킹을 안 해서 성능 좋음
 *   - 쓰기 작업에는 @Transactional 따로 붙여야 함
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqServiceImpl implements FaqService {

    /**
     * Repository 주입 (의존성 주입, DI)
     * final + @RequiredArgsConstructor = 생성자 주입 방식
     */
    private final FaqRepository faqRepository;

    /**
     * FAQ 생성
     * 
     * @Transactional: 쓰기 작업이므로 읽기 전용 해제
     * 
     * 처리 흐름:
     * 1. DTO에서 데이터 꺼내서 Entity 생성 (Builder 패턴)
     * 2. Repository로 DB에 저장
     * 3. 저장된 Entity를 응답 DTO로 변환해서 반환
     */
    @Override
    @Transactional
    public FaqResponseDTO.Create create(FaqRequestDTO.Create dto) {
        // 1. DTO → Entity 변환 (Builder 패턴 사용)
        Faq faq = Faq.builder()
                .question(dto.question())      // record는 .question() 으로 접근
                .answer(dto.answer())
                .category(dto.category())
                .displayOrder(dto.displayOrder())
                .build();

        // 2. DB에 저장
        Faq savedFaq = faqRepository.save(faq);

        // 3. Entity → 응답 DTO 변환 후 반환
        return FaqResponseDTO.Create.from(savedFaq);
    }

    /**
     * FAQ 단건 조회
     * 
     * 처리 흐름:
     * 1. Repository에서 ID로 조회 (없으면 에러)
     * 2. Entity를 응답 DTO로 변환해서 반환
     */
    @Override
    public FaqResponseDTO.GetOne getById(Long id) {
        // getById는 없으면 CustomException 발생
        Faq faq = faqRepository.getById(id);
        return FaqResponseDTO.GetOne.from(faq);
    }

    /**
     * FAQ 전체 조회
     * 
     * 처리 흐름:
     * 1. Repository에서 전체 조회
     * 2. 각 Entity를 DTO로 변환 (Stream API 사용)
     * 
     * Stream 설명:
     * - .stream(): 리스트를 스트림으로 변환
     * - .map(): 각 요소를 변환 (Faq → FaqResponseDTO.GetOne)
     * - .collect(): 다시 리스트로 모음
     */
    @Override
    public List<FaqResponseDTO.GetOne> findAll() {
        List<Faq> faqs = faqRepository.findAll();

        return faqs.stream()
                .map(FaqResponseDTO.GetOne::from)  // 메서드 레퍼런스 (람다식 축약)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리별 FAQ 조회
     */
    @Override
    public List<FaqResponseDTO.GetOne> findByCategory(String category) {
        List<Faq> faqs = faqRepository.findByCategory(category);

        return faqs.stream()
                .map(FaqResponseDTO.GetOne::from)
                .collect(Collectors.toList());
    }

    /**
     * FAQ 수정
     * 
     * @Transactional: 쓰기 작업
     * 
     * 처리 흐름:
     * 1. 기존 FAQ 조회
     * 2. Entity의 값 수정 (updateFaq 메서드)
     * 3. 저장 (JPA 변경 감지로 자동 UPDATE)
     * 4. 응답 DTO 반환
     * 
     * JPA 변경 감지(Dirty Checking):
     * - @Transactional 안에서 Entity 값을 바꾸면
     * - 트랜잭션 끝날 때 JPA가 자동으로 UPDATE 쿼리 실행
     */
    @Override
    @Transactional
    public FaqResponseDTO.GetOne update(Long id, FaqRequestDTO.Update dto) {
        // 1. 기존 데이터 조회 (없으면 에러)
        Faq faq = faqRepository.getById(id);

        // 2. 값 수정 (Entity 메서드 사용)
        faq.updateFaq(
                dto.question(),
                dto.answer(),
                dto.category(),
                dto.displayOrder()
        );

        // 3. 저장 (변경 감지로 UPDATE 실행됨)
        Faq updatedFaq = faqRepository.save(faq);

        // 4. 응답 DTO 반환
        return FaqResponseDTO.GetOne.from(updatedFaq);
    }

    /**
     * FAQ 삭제
     * 
     * @Transactional: 쓰기 작업
     * 
     * 처리 흐름:
     * 1. 존재하는지 먼저 확인 (없으면 에러)
     * 2. 삭제
     */
    @Override
    @Transactional
    public void delete(Long id) {
        // 존재하는지 확인 (없으면 getById에서 에러)
        faqRepository.getById(id);

        // 삭제
        faqRepository.delete(id);
    }
}
