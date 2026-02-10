package SMU.BAMBOO.Hompage.domain.faq.service;

import SMU.BAMBOO.Hompage.domain.faq.dto.FaqRequestDTO;
import SMU.BAMBOO.Hompage.domain.faq.dto.FaqResponseDTO;

import java.util.List;

/**
 * FAQ Service 인터페이스
 * 
 * 왜 인터페이스로 만드나?
 * 1. Controller는 이 인터페이스만 알면 됨
 * 2. 구현 방식이 바뀌어도 Controller 수정 없음
 * 3. 테스트할 때 가짜 구현(Mock)으로 교체 가능
 * 
 * 각 메서드는 하나의 "기능"을 나타냄
 */
public interface FaqService {

    /**
     * FAQ 생성
     * @param dto 생성 요청 데이터
     * @return 생성된 FAQ 정보
     */
    FaqResponseDTO.Create create(FaqRequestDTO.Create dto);

    /**
     * FAQ 단건 조회
     * @param id FAQ ID
     * @return FAQ 상세 정보
     */
    FaqResponseDTO.GetOne getById(Long id);

    /**
     * FAQ 전체 조회 (표시 순서대로)
     * @return FAQ 목록
     */
    List<FaqResponseDTO.GetOne> findAll();

    /**
     * 카테고리별 FAQ 조회
     * @param category 카테고리명
     * @return 해당 카테고리의 FAQ 목록
     */
    List<FaqResponseDTO.GetOne> findByCategory(String category);

    /**
     * FAQ 수정
     * @param id FAQ ID
     * @param dto 수정 요청 데이터
     * @return 수정된 FAQ 정보
     */
    FaqResponseDTO.GetOne update(Long id, FaqRequestDTO.Update dto);

    /**
     * FAQ 삭제
     * @param id FAQ ID
     */
    void delete(Long id);
}
