package SMU.BAMBOO.Hompage.domain.mainActivites.controller;

import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesResponseDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.entity.MainActivities;
import SMU.BAMBOO.Hompage.domain.mainActivites.service.MainActivitiesService;
import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import SMU.BAMBOO.Hompage.global.upload.AwsS3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "주요활동 게시판 API")
public class MainActivitiesController {

    private final MainActivitiesService mainActivitiesService;
    private final AwsS3Service awsS3Service;

    /** 주요활동 게시판 게시물 생성 API */
    @PostMapping(value = "/api/main-activities", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "주요활동 게시판 게시물 생성 (이미지 업로드는 Postman에서 테스트해주세요)")
    public SuccessResponse<MainActivitiesResponseDTO.Detail> createMainActivities(
            @Valid @ModelAttribute MainActivitiesRequestDTO.Create request,
            @CurrentMember Member member) {

        List<String> images = new ArrayList<>();
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            images = awsS3Service.uploadFiles("main-activities", request.getImages());
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
    @PatchMapping("/api/main-activities/{id}")
    @Operation(summary = "주요활동 게시물 수정 (이미지 업로드는 Postman에서 테스트해주세요)")
    public SuccessResponse<String> updateMainActivity(
            @PathVariable Long id,
            @Valid @ModelAttribute MainActivitiesRequestDTO.Update request,
            @CurrentMember Member member) {

        List<String> images = new ArrayList<>();
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            images = awsS3Service.uploadFiles("main-activities", request.getImages());
        }
        mainActivitiesService.updateMainActivity(id, request, images, member);

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
