package SMU.BAMBOO.Hompage.domain.knowledge.controller;

import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeRequestDTO;
import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeResponseDTO;
import SMU.BAMBOO.Hompage.domain.knowledge.service.KnowledgeService;
import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "지식 공유 게시판 API")
@RequestMapping("/api/knowledges")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "지식 공유 게시판 게시물 생성 (이미지 & 파일 업로드는 Postman에서 테스트)")
    public SuccessResponse<KnowledgeResponseDTO.Create> createKnowledge(
            @Valid @ModelAttribute KnowledgeRequestDTO.Create request,
            @RequestPart(required = false) List<MultipartFile> images,
            @RequestPart(required = false) List<MultipartFile> files,
            @CurrentMember Member member) {

        // 빈 문자열로 들어온 경우 null 처리
        if (images != null && images.size() == 1 && images.get(0).isEmpty()) {
            images = null;
        }
        if (files != null && files.size() == 1 && files.get(0).isEmpty()) {
            files = null;
        }

        KnowledgeResponseDTO.Create response = knowledgeService.create(request, member, images, files);
        return SuccessResponse.ok(response);
    }


    @GetMapping
    @Operation(summary = "지식 공유 게시판 목록 조회 (type 필터 및 검색어 적용 가능)")
    public SuccessResponse<Page<KnowledgeResponseDTO.GetOne>> getKnowledges(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<KnowledgeResponseDTO.GetOne> knowledgeList = knowledgeService.getKnowledges(type, keyword, page - 1, size);
        return SuccessResponse.ok(knowledgeList);
    }


    @GetMapping("/{id}")
    @Operation(summary = "지식 공유 게시판 게시물 단일 조회")
    public SuccessResponse<KnowledgeResponseDTO.GetOne> getKnowledge(@PathVariable Long id) {
        KnowledgeResponseDTO.GetOne response = knowledgeService.getById(id);
        return SuccessResponse.ok(response);
    }


    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "지식 공유 게시물 수정 (기존 URL은 JSON 배열, 새 파일은 Multipart로 전송)")
    public SuccessResponse<KnowledgeResponseDTO.Update> updateKnowledge(
            @PathVariable Long id,
            @Valid @ModelAttribute KnowledgeRequestDTO.Update request,
            @RequestPart(required = false) String imageUrls,  // 기존 이미지 URL을 JSON으로 받음
            @RequestPart(required = false) List<MultipartFile> newImages, // 새 이미지 파일
            @RequestPart(required = false) String fileUrls,
            @RequestPart(required = false) List<MultipartFile> newFiles) {

        List<Object> finalImages = new ArrayList<>();
        List<Object> finalFiles = new ArrayList<>();

        // 기존 이미지 URL JSON 파싱
        if (imageUrls != null && !imageUrls.isEmpty()) {
            try {
                List<String> existingImageUrls = objectMapper.readValue(imageUrls, new TypeReference<List<String>>() {});
                finalImages.addAll(existingImageUrls);
            } catch (JsonProcessingException e) {
                throw new CustomException(ErrorCode.INVALID_URL);
            }
        }

        // 기존 파일 URL JSON 파싱
        if (fileUrls != null && !fileUrls.isEmpty()) {
            try {
                List<String> existingFileUrls = objectMapper.readValue(fileUrls, new TypeReference<List<String>>() {});
                finalFiles.addAll(existingFileUrls);
            } catch (JsonProcessingException e) {
                throw new CustomException(ErrorCode.INVALID_URL);
            }
        }

        // 새 이미지 추가
        if (newImages != null && !newImages.isEmpty()) {
            finalImages.addAll(newImages);
        }

        // 새 파일 추가
        if (newFiles != null && !newFiles.isEmpty()) {
            finalFiles.addAll(newFiles);
        }

        KnowledgeResponseDTO.Update result = knowledgeService.update(id, request, finalImages, finalFiles);
        return SuccessResponse.ok(result);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "지식 공유 게시물 삭제")
    public SuccessResponse<String> deleteKnowledge(@PathVariable Long id) {
        knowledgeService.delete(id);
        return SuccessResponse.ok("지식 공유 게시판 게시물이 삭제되었습니다.");
    }
}

