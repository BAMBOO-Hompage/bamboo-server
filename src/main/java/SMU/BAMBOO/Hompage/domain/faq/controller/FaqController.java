package SMU.BAMBOO.Hompage.domain.faq.controller;

import SMU.BAMBOO.Hompage.domain.faq.dto.FaqRequestDTO;
import SMU.BAMBOO.Hompage.domain.faq.dto.FaqResponseDTO;
import SMU.BAMBOO.Hompage.domain.faq.service.FaqService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FAQ Controller - API 엔드포인트 정의
 * 
 * @RestController: REST API 컨트롤러 (JSON 응답)
 *   - @Controller + @ResponseBody 합친 것
 * 
 * @RequestMapping("/api/faqs"): 기본 URL 경로
 *   - 모든 메서드의 URL 앞에 /api/faqs 가 붙음
 * 
 * @Tag: Swagger UI에서 이 API들을 그룹으로 묶어서 표시
 */
@RestController
@RequestMapping("/api/faqs")
@RequiredArgsConstructor
@Tag(name = "FAQ API", description = "자주 묻는 질문 관련 API")
public class FaqController {

    /**
     * Service 주입
     * Controller는 Service만 호출함 (Repository 직접 접근 X)
     */
    private final FaqService faqService;

    /**
     * FAQ 생성
     * 
     * HTTP: POST /api/faqs
     * 
     * @PostMapping: POST 요청 처리
     * @Operation: Swagger 문서에 표시될 API 설명
     * @Valid: DTO의 유효성 검사 실행 (@NotBlank 등)
     * @RequestBody: HTTP 요청 본문(JSON)을 DTO로 변환
     * 
     * SuccessResponse: 응답 형식 통일 (이 프로젝트의 규칙)
     */
    @PostMapping
    @Operation(summary = "FAQ 생성", description = "새로운 FAQ를 생성합니다.")
    public SuccessResponse<FaqResponseDTO.Create> create(
            @Valid @RequestBody FaqRequestDTO.Create request
    ) {
        FaqResponseDTO.Create result = faqService.create(request);
        return SuccessResponse.ok(result);
    }

    /**
     * FAQ 단건 조회
     * 
     * HTTP: GET /api/faqs/{faqId}
     * 예시: GET /api/faqs/1 → ID가 1인 FAQ 조회
     * 
     * @GetMapping("/{faqId}"): GET 요청, URL에 변수 포함
     * @PathVariable: URL의 {faqId} 값을 파라미터로 받음
     * @Parameter: Swagger 문서에 파라미터 설명 추가
     */
    @GetMapping("/{faqId}")
    @Operation(summary = "FAQ 단건 조회", description = "ID로 특정 FAQ를 조회합니다.")
    public SuccessResponse<FaqResponseDTO.GetOne> getById(
            @Parameter(description = "FAQ ID", example = "1")
            @PathVariable("faqId") Long id
    ) {
        FaqResponseDTO.GetOne result = faqService.getById(id);
        return SuccessResponse.ok(result);
    }

    /**
     * FAQ 전체 조회
     * 
     * HTTP: GET /api/faqs
     * 
     * 표시 순서(displayOrder)대로 정렬되어 반환됨
     */
    @GetMapping
    @Operation(summary = "FAQ 전체 조회", description = "모든 FAQ를 표시 순서대로 조회합니다.")
    public SuccessResponse<List<FaqResponseDTO.GetOne>> findAll() {
        List<FaqResponseDTO.GetOne> result = faqService.findAll();
        return SuccessResponse.ok(result);
    }

    /**
     * 카테고리별 FAQ 조회
     * 
     * HTTP: GET /api/faqs/category/{category}
     * 예시: GET /api/faqs/category/가입 → "가입" 카테고리 FAQ만 조회
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "카테고리별 FAQ 조회", description = "특정 카테고리의 FAQ를 조회합니다.")
    public SuccessResponse<List<FaqResponseDTO.GetOne>> findByCategory(
            @Parameter(description = "카테고리명", example = "가입")
            @PathVariable("category") String category
    ) {
        List<FaqResponseDTO.GetOne> result = faqService.findByCategory(category);
        return SuccessResponse.ok(result);
    }

    /**
     * FAQ 수정
     * 
     * HTTP: PATCH /api/faqs/{faqId}
     * 
     * @PatchMapping: 부분 수정 (전체 수정은 PUT)
     * 
     * PATCH vs PUT:
     * - PUT: 전체 교체 (모든 필드 필수)
     * - PATCH: 부분 수정 (보낸 필드만 수정)
     */
    @PatchMapping("/{faqId}")
    @Operation(summary = "FAQ 수정", description = "FAQ를 수정합니다. 변경할 필드만 보내면 됩니다.")
    public SuccessResponse<FaqResponseDTO.GetOne> update(
            @Parameter(description = "FAQ ID", example = "1")
            @PathVariable("faqId") Long id,
            @Valid @RequestBody FaqRequestDTO.Update request
    ) {
        FaqResponseDTO.GetOne result = faqService.update(id, request);
        return SuccessResponse.ok(result);
    }

    /**
     * FAQ 삭제
     * 
     * HTTP: DELETE /api/faqs/{faqId}
     * 
     * @DeleteMapping: DELETE 요청 처리
     */
    @DeleteMapping("/{faqId}")
    @Operation(summary = "FAQ 삭제", description = "FAQ를 삭제합니다.")
    public SuccessResponse<String> delete(
            @Parameter(description = "FAQ ID", example = "1")
            @PathVariable("faqId") Long id
    ) {
        faqService.delete(id);
        return SuccessResponse.ok("FAQ가 삭제되었습니다.");
    }
}
