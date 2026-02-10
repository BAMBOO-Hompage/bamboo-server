package SMU.BAMBOO.Hompage.domain.faq.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * FAQ 요청 DTO
 * 클라이언트가 서버로 보내는 데이터 형식
 * 
 * record: Java 16+ 문법, 간단한 데이터 클래스
 *         자동으로 생성자, getter, equals, hashCode, toString 생성됨
 */
@Schema(description = "FAQ 요청 DTO")
public class FaqRequestDTO {

    /**
     * FAQ 생성 요청 DTO
     * POST /api/faqs 할 때 사용
     */
    @Schema(description = "FAQ 생성 요청")
    public record Create(
            /**
             * @NotBlank: null, "", " " 모두 불가
             * @Schema: Swagger 문서에 표시될 설명과 예시
             */
            @NotBlank(message = "질문은 필수입니다")
            @Schema(description = "질문", example = "동아리 가입은 어떻게 하나요?")
            String question,

            @NotBlank(message = "답변은 필수입니다")
            @Schema(description = "답변", example = "매 학기 초 모집 공고를 통해 지원하실 수 있습니다.")
            String answer,

            @NotBlank(message = "카테고리는 필수입니다")
            @Schema(description = "카테고리", example = "가입")
            String category,

            /**
             * @NotNull: null 불가 (숫자형이라 NotBlank 대신 사용)
             * @Positive: 양수만 허용 (1 이상)
             */
            @NotNull(message = "표시 순서는 필수입니다")
            @Positive(message = "표시 순서는 1 이상이어야 합니다")
            @Schema(description = "표시 순서 (숫자가 작을수록 위에 표시)", example = "1")
            Integer displayOrder
    ) {}

    /**
     * FAQ 수정 요청 DTO
     * PATCH /api/faqs/{id} 할 때 사용
     * 
     * 수정은 일부만 변경할 수 있으므로 @NotBlank 없음
     * null인 필드는 수정하지 않음
     */
    @Schema(description = "FAQ 수정 요청")
    public record Update(
            @Schema(description = "질문", example = "수정된 질문입니다")
            String question,

            @Schema(description = "답변", example = "수정된 답변입니다")
            String answer,

            @Schema(description = "카테고리", example = "스터디")
            String category,

            @Schema(description = "표시 순서", example = "2")
            Integer displayOrder
    ) {}
}
