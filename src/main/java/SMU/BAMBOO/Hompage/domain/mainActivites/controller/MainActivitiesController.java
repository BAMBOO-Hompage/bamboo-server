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
            throw new CustomException(ErrorCode.INVALID_REQUEST, "이미지가 null입니다.");
        }
        List<String> images = awsS3Service.uploadFile(request.getImages());
        MainActivitiesResponseDTO.Create response = mainActivitiesService.create(request, images);
        return SuccessResponse.ok(response);
    }


    /** 주요활동 게시판 게시물 단일 조회 (+ 조회수 증가) API */

    /** 주요활동 게시판 게시물 전체 조회 API */

    /** 주요활동 게시판 게시물 수정 API */

    /** 주요활동 게시판 게시물 삭제 API */
}
