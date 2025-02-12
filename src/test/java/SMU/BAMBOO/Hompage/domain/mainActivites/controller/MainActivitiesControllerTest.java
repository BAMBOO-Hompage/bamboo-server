package SMU.BAMBOO.Hompage.domain.mainActivites.controller;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.mock.container.TestContainer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MainActivitiesControllerTest {

    private TestContainer testContainer;
    private Member testMember;

    @BeforeEach
    void setUp() {
        testContainer = new TestContainer();

        testMember = Member.builder()
                .memberId(1L)
                .studentId("202010766")
                .email("kjk@example.com")
                .name("김재관")
                .major("휴먼지능정보공학과")
                .role(Role.ROLE_ADMIN)
                .build();
    }

    @Test
    @DisplayName("주요 활동 게시물 생성 성공")
    void Success_Create_MainActivities() {
        // Given
        MainActivitiesRequestDTO.Create request = new MainActivitiesRequestDTO.Create();
        request.setTitle("BAMBOO 프로젝트");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 2, 28));
        request.setYear(2025);

        MockMultipartFile imageFile = new MockMultipartFile(
                "images",
                "test-image.png",
                "image/png",
                "test-image-content".getBytes()
        );

        List<MultipartFile> images = List.of(imageFile);
        request.setImages(images);

        when(testContainer.awsS3Service.uploadFiles(anyString(), anyList(), anyBoolean()))
                .thenReturn(List.of("https://s3.aws.com/image1.png"));

        // When
        SuccessResponse<MainActivitiesResponseDTO.Detail> response =
                testContainer.mainActivitiesController.createMainActivities(request, testMember);

        // Then
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getTitle()).isEqualTo("BAMBOO 프로젝트");
        assertThat(response.getResult().getStartDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(response.getResult().getEndDate()).isEqualTo(LocalDate.of(2025, 2, 28));
        assertThat(response.getResult().getYear()).isEqualTo(2025);
        assertThat(response.getResult().getImages()).containsExactly("https://s3.aws.com/image1.png");
        assertThat(response.getResult().getMemberName()).isEqualTo(testMember.getName());

        verify(testContainer.awsS3Service, times(1)).uploadFiles(anyString(), anyList(), anyBoolean());
    }

    @Test
    @DisplayName("권한이 없는 사용자가 게시물 생성 시 예외 발생")
    void createMainActivity_NoPermission_Fail() {
        // Given
        Member unauthorizedUser = Member.builder()
                .memberId(2L)
                .studentId("202010767")
                .email("kkk@example.com")
                .name("짭재관")
                .major("휴먼지능정보공학과")
                .role(Role.ROLE_USER)
                .build();

        MainActivitiesRequestDTO.Create request = new MainActivitiesRequestDTO.Create();
        request.setTitle("[권한X] BAMBOO 프로젝트");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 2, 28));
        request.setYear(2025);

        // When
        // Then
        assertThatThrownBy(() -> testContainer.mainActivitiesController.createMainActivities(request, unauthorizedUser))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NO_PERMISSION.getMessage());
    }

    @Test
    @DisplayName("연도별 주요 활동 조회 성공")
    void Success_Get_MainActivities_By_Year() {
        // Given
        MainActivitiesRequestDTO.Create request1 = new MainActivitiesRequestDTO.Create();
        request1.setTitle("BAMBOO 프로젝트 개발");
        request1.setStartDate(LocalDate.of(2025, 1, 1));
        request1.setEndDate(LocalDate.of(2025, 2, 28));
        request1.setYear(2025);

        MainActivitiesRequestDTO.Create request2 = new MainActivitiesRequestDTO.Create();
        request2.setTitle("스터디 활동");
        request2.setStartDate(LocalDate.of(2025, 3, 4));
        request2.setEndDate(LocalDate.of(2025, 5, 30));
        request2.setYear(2025);

        testContainer.mainActivitiesService.create(request1, List.of(), testMember);
        testContainer.mainActivitiesService.create(request2, List.of(), testMember);

        // When
        SuccessResponse<Page<MainActivitiesResponseDTO.ActivitiesByYearResponse>> response =
                testContainer.mainActivitiesController.getMainActivitiesByYear(2025, 1, 10);

        // Then
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult()).hasSize(2);
        assertThat(response.getResult()).extracting("title")
                .containsExactlyInAnyOrder("BAMBOO 프로젝트 개발", "스터디 활동");
        assertThat(response.getResult().getContent()).extracting("year").containsOnly(2025);
        assertThat(response.getResult().getTotalPages()).isGreaterThanOrEqualTo(1);
    }


    @Test
    @DisplayName("단일 주요 활동 조회 성공")
    void getMainActivity_Success() {
        // Given
        MainActivitiesRequestDTO.Create request = new MainActivitiesRequestDTO.Create();
        request.setTitle("BAMBOO 프로젝트");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 2, 28));
        request.setYear(2025);

        SuccessResponse<MainActivitiesResponseDTO.Detail> createdResponse =
                testContainer.mainActivitiesController.createMainActivities(request, testMember);

        Long activityId = createdResponse.getResult().getMainActivitiesId();

        // When
        SuccessResponse<MainActivitiesResponseDTO.Detail> response =
                testContainer.mainActivitiesController.getMainActivity(activityId);

        // Then
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getTitle()).isEqualTo("BAMBOO 프로젝트");
        assertThat(response.getResult().getStartDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(response.getResult().getEndDate()).isEqualTo(LocalDate.of(2025, 2, 28));
        assertThat(response.getResult().getYear()).isEqualTo(2025);
        assertThat(response.getResult().getMemberName()).isEqualTo(testMember.getName());
    }

    @Test
    @DisplayName("존재하지 않는 주요 활동 게시물 조회 시 예외 발생")
    void GetMainActivity_Not_Exist_User() {
        // When
        // Then
        assertThatThrownBy(() -> testContainer.mainActivitiesController.getMainActivity(999L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST.getMessage());
    }

    @Test
    @DisplayName("주요 활동 게시물 수정 성공")
    void updateMainActivity_Success() throws JsonProcessingException {
        // Given
        MainActivitiesRequestDTO.Create request = new MainActivitiesRequestDTO.Create();
        request.setTitle("BAMBOO 프로젝트");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 2, 28));
        request.setYear(2025);

        SuccessResponse<MainActivitiesResponseDTO.Detail> createdResponse =
                testContainer.mainActivitiesController.createMainActivities(request, testMember);

        Long activityId = createdResponse.getResult().getMainActivitiesId();

        MainActivitiesRequestDTO.Update updateRequest = new MainActivitiesRequestDTO.Update();
        updateRequest.setTitle("[수정] BAMBOO 프로젝트");
        updateRequest.setYear(2025);

        // 기존 이미지 URL을 JSON 문자열로 변환하여 전달
        ObjectMapper objectMapper = new ObjectMapper();
        String imageUrlsJson = objectMapper.writeValueAsString(List.of("https://s3.aws.com/old_image.png"));

        // 새로운 이미지 파일(MockMultipartFile) 추가
        MockMultipartFile newImageFile = new MockMultipartFile(
                "newImages",
                "test.png",
                "image/png",
                "test-image-content".getBytes()
        );

        List<MultipartFile> newImages = List.of(newImageFile);

        when(testContainer.awsS3Service.uploadFile(anyString(), any(), anyBoolean()))
                .thenReturn("https://s3.aws.com/new_image.png");

        // When
        SuccessResponse<String> updateResponse = testContainer.mainActivitiesController
                .updateMainActivity(activityId, updateRequest, imageUrlsJson, newImages, testMember);

        SuccessResponse<MainActivitiesResponseDTO.Detail> updatedResponse =
                testContainer.mainActivitiesController.getMainActivity(activityId);

        // Then
        assertThat(updateResponse.getResult()).isEqualTo("주요 활동 게시판 게시물이 수정되었습니다.");
        assertThat(updatedResponse.getResult()).isNotNull();
        assertThat(updatedResponse.getResult().getTitle()).isEqualTo("[수정] BAMBOO 프로젝트");
        assertThat(updatedResponse.getResult().getYear()).isEqualTo(2025);

        assertThat(updatedResponse.getResult().getImages()).containsExactlyInAnyOrder(
                "https://s3.aws.com/old_image.png", // 기존 이미지
                "https://s3.aws.com/new_image.png"  // 새로 업로드된 이미지
        );

        verify(testContainer.awsS3Service, times(1))
                .uploadFile(anyString(), any(), anyBoolean());
    }

    @Test
    @DisplayName("권한이 없는 사용자가 게시물 수정 시 예외 발생")
    void updateMainActivity_NoPermission_Fail() throws JsonProcessingException {
        // Given
        Member unauthorizedUser = Member.builder()
                .memberId(2L)
                .studentId("202010767")
                .email("kkk@example.com")
                .name("짭재관")
                .major("휴먼지능정보공학과")
                .role(Role.ROLE_USER)
                .build();

        MainActivitiesRequestDTO.Create request = new MainActivitiesRequestDTO.Create();
        request.setTitle("BAMBOO 프로젝트");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 2, 28));
        request.setYear(2025);

        SuccessResponse<MainActivitiesResponseDTO.Detail> createdResponse =
                testContainer.mainActivitiesController.createMainActivities(request, testMember);
        Long activityId = createdResponse.getResult().getMainActivitiesId();

        MainActivitiesRequestDTO.Update updateRequest = new MainActivitiesRequestDTO.Update();
        updateRequest.setTitle("[수정] BAMBOO 프로젝트");
        updateRequest.setYear(2025);

        // When
        // Then
        assertThatThrownBy(() -> testContainer.mainActivitiesController.updateMainActivity(activityId, updateRequest, "[]", List.of(), unauthorizedUser))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NO_PERMISSION.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 게시물 수정 시 예외 발생")
    void updateMainActivity_NotExist_Fail() throws JsonProcessingException {
        // Given
        MainActivitiesRequestDTO.Update updateRequest = new MainActivitiesRequestDTO.Update();
        updateRequest.setTitle("[수정] BAMBOO 프로젝트");
        updateRequest.setYear(2025);

        // When & Then
        assertThatThrownBy(() -> testContainer.mainActivitiesController.updateMainActivity(999L, updateRequest, "[]", List.of(), testMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST.getMessage());
    }

    @Test
    @DisplayName("주요 활동 게시물 삭제 성공")
    void deleteMainActivity_Success() {
        // Given
        MainActivitiesRequestDTO.Create request = new MainActivitiesRequestDTO.Create();
        request.setTitle("BAMBOO 프로젝트");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 2, 28));
        request.setYear(2025);

        SuccessResponse<MainActivitiesResponseDTO.Detail> createdResponse =
                testContainer.mainActivitiesController.createMainActivities(request, testMember);

        Long activityId = createdResponse.getResult().getMainActivitiesId();

        // When
        testContainer.mainActivitiesController.deleteMainActivity(activityId, testMember);

        // Then
        assertThatThrownBy(() -> testContainer.mainActivitiesController.getMainActivity(activityId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST.getMessage());
    }

    @Test
    @DisplayName("권한이 없는 사용자가 게시물 삭제 시 예외 발생")
    void deleteMainActivity_NoPermission() {
        // Given
        Member unauthorizedUser = Member.builder()
                .memberId(2L)
                .studentId("202010767")
                .email("kkk@example.com")
                .name("짭재관")
                .major("휴먼지능정보공학과")
                .role(Role.ROLE_USER)
                .build();

        MainActivitiesRequestDTO.Create request = new MainActivitiesRequestDTO.Create();
        request.setTitle("BAMBOO 프로젝트");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 2, 28));
        request.setYear(2025);

        SuccessResponse<MainActivitiesResponseDTO.Detail> createdResponse =
                testContainer.mainActivitiesController.createMainActivities(request, testMember);

        Long activityId = createdResponse.getResult().getMainActivitiesId();

        // When
        // Then
        assertThatThrownBy(() -> testContainer.mainActivitiesController.deleteMainActivity(activityId, unauthorizedUser))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NO_PERMISSION.getMessage());
    }

}