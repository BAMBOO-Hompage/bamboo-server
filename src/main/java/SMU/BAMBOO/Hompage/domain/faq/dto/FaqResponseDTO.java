package SMU.BAMBOO.Hompage.domain.faq.dto;

import SMU.BAMBOO.Hompage.domain.faq.entity.Faq;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * FAQ 응답 DTO
 * 서버가 클라이언트에 보내는 데이터 형식
 * 
 * Entity를 직접 반환하지 않고 DTO로 변환해서 반환하는 이유:
 * 1. 필요한 정보만 선택적으로 노출
 * 2. Entity 변경이 API 응답에 영향 주지 않음
 * 3. 순환 참조 방지 (연관관계 있을 때)
 */
@Schema(description = "FAQ 응답 DTO")
public class FaqResponseDTO {

    /**
     * FAQ 생성 응답 DTO
     * 생성 후 간단한 정보만 반환
     */
    @Schema(description = "FAQ 생성 응답")
    public record Create(
            @Schema(description = "FAQ ID") Long faqId,
            @Schema(description = "질문") String question,
            @Schema(description = "카테고리") String category
    ) {
        /**
         * Entity → DTO 변환 메서드 (정적 팩토리 메서드)
         * 
         * 왜 static 메서드로 만드나?
         * - 변환 로직을 DTO 안에 캡슐화
         * - Service에서 간단하게 호출 가능: FaqResponseDTO.Create.from(entity)
         */
        public static Create from(Faq faq) {
            return new Create(
                    faq.getFaqId(),
                    faq.getQuestion(),
                    faq.getCategory()
            );
        }
    }

    /**
     * FAQ 단건 조회 응답 DTO
     * 모든 정보 포함
     */
    @Schema(description = "FAQ 조회 응답")
    public record GetOne(
            @Schema(description = "FAQ ID") Long faqId,
            @Schema(description = "질문") String question,
            @Schema(description = "답변") String answer,
            @Schema(description = "카테고리") String category,
            @Schema(description = "표시 순서") Integer displayOrder,
            @Schema(description = "생성일") LocalDateTime createdAt,
            @Schema(description = "수정일") LocalDateTime modifiedAt
    ) {
        public static GetOne from(Faq faq) {
            return new GetOne(
                    faq.getFaqId(),
                    faq.getQuestion(),
                    faq.getAnswer(),
                    faq.getCategory(),
                    faq.getDisplayOrder(),
                    faq.getCreatedAt(),     // BaseEntity에서 상속
                    faq.getModifiedAt()     // BaseEntity에서 상속 (updatedAt이 아닌 modifiedAt)
            );
        }
    }

    /**
     * FAQ 목록 조회용 간단 응답 DTO
     * 목록에서는 답변 전체를 보여줄 필요 없을 때 사용
     */
    @Schema(description = "FAQ 목록 조회 응답")
    public record GetList(
            @Schema(description = "FAQ ID") Long faqId,
            @Schema(description = "질문") String question,
            @Schema(description = "카테고리") String category,
            @Schema(description = "표시 순서") Integer displayOrder
    ) {
        public static GetList from(Faq faq) {
            return new GetList(
                    faq.getFaqId(),
                    faq.getQuestion(),
                    faq.getCategory(),
                    faq.getDisplayOrder()
            );
        }
    }
}
