package SMU.BAMBOO.Hompage.domain.mainActivites.controller;

import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesResponseDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.service.MainActivitiesService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.AwsS3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "주요활동 게시판 API")
public class MainActivitiesController {

    private final MainActivitiesService mainActivitiesService;
    private final AwsS3Service awsS3Service;

    /** 주요활동 게시판 게시물 생성 API */
    @PostMapping(value = "/api/main-activities", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "주요활동 게시판 게시물 생성")
    public SuccessResponse<MainActivitiesResponseDTO.Create> createMainActivities(
            @Valid @ModelAttribute MainActivitiesRequestDTO.Create request) {
        if (request.getImages() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        List<String> images = awsS3Service.uploadFile("main-activities", request.getImages());
        MainActivitiesResponseDTO.Create response = mainActivitiesService.create(request, images);
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

    /** 주요활동 게시판 게시물 수정 API */
    @PatchMapping("/api/main-activities/{id}")
    @Operation(summary = "주요활동 게시물 수정")
    public SuccessResponse<String> updateMainActivity(
            @PathVariable Long id,
            @Valid @ModelAttribute MainActivitiesRequestDTO.Update request){
        if (request.getImages() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        List<String> images = awsS3Service.uploadFile("main-activities", request.getImages());
        mainActivitiesService.updateMainActivity(id, request, images);
        return SuccessResponse.ok("주요 활동 게시판 게시물이 수정되었습니다.");
    }


    /** 주요활동 게시판 게시물 삭제 API */
    @DeleteMapping("/api/main-activities/{id}")
    @Operation(summary = "주요활동 게시물 삭제")
    public SuccessResponse<String> deleteMainActivity(@PathVariable Long id){
        mainActivitiesService.deleteMainActivity(id);
        return SuccessResponse.ok("주요 활동 게시판 게시물이 삭제되었습니다.");
    }
}
