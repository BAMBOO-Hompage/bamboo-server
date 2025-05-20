package SMU.BAMBOO.Hompage.domain.mainActivites.controller;

import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesResponseDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.service.MainActivitiesService;
import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Facade;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Builder
@Tag(name = "주요활동 게시판 API")
public class MainActivitiesController {

    private final MainActivitiesService mainActivitiesService;
    private final AwsS3Facade awsS3Facade;
    private final ObjectMapper objectMapper;

    /** 주요활동 게시판 게시물 생성 API */
    @PostMapping(value = "/api/main-activities", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "주요활동 게시판 게시물 생성")
    public SuccessResponse<MainActivitiesResponseDTO.Detail> createMainActivities(
            @Valid @ModelAttribute MainActivitiesRequestDTO.Create request,
            @CurrentMember Member member
    ) {
        List<String> images = new ArrayList<>();

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            images = awsS3Facade.uploadFiles("main-activities", request.getImages(), true);
        }
        MainActivitiesResponseDTO.Detail response = mainActivitiesService.create(request, images, member);

        return SuccessResponse.ok(response);
    }

    /** 주요활동 게시판 게시물 연도별 조회 API */
    @GetMapping(value = "/api/main-activities/year")
    @Operation(summary = "연도별 주요활동 게시판 조회")
    public SuccessResponse<Page<MainActivitiesResponseDTO.ActivitiesByYearResponse>> getMainActivitiesByYear(
            @RequestParam("year") int year,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "3") int size) {
        Page<MainActivitiesResponseDTO.ActivitiesByYearResponse> activities = mainActivitiesService.getMainActivitiesByYear(year, page - 1, size);
        return SuccessResponse.ok(activities);
    }

    /** 주요활동 게시판 게시물 단일 조회 API */
    @GetMapping(value = "/api/main-activities/{id}")
    @Operation(summary = "주요활동 게시물 단일 조회")
    public SuccessResponse<MainActivitiesResponseDTO.Detail> getMainActivity(@PathVariable Long id) {
        MainActivitiesResponseDTO.Detail response = mainActivitiesService.getMainActivity(id);
        return SuccessResponse.ok(response);
    }

    /** 주요활동 게시판 게시물 수정 API */
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE)))
    @PatchMapping(value = "/api/main-activities/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "주요활동 게시물 수정 (기존 이미지 URL은 JSON 배열, 새 이미지는 Multipart로 전송)")
    public SuccessResponse<String> updateMainActivity(
            @PathVariable Long id,
            @RequestPart("request") @Valid MainActivitiesRequestDTO.Update request,
            @RequestPart(required = false) List<MultipartFile> newImages,
            @CurrentMember Member member) {
        mainActivitiesService.updateMainActivity(id, request, newImages, member);
        return SuccessResponse.ok("주요 활동 게시판 게시물이 수정되었습니다.");
    }


    /** 주요활동 게시판 게시물 삭제 API */
    @DeleteMapping("/api/main-activities/{id}")
    @Operation(summary = "주요활동 게시물 삭제")
    public SuccessResponse<String> deleteMainActivity(@PathVariable Long id, @CurrentMember Member member) {

        mainActivitiesService.deleteMainActivity(id, member);

        return SuccessResponse.ok("주요 활동 게시판 게시물이 삭제되었습니다.");
    }
}
