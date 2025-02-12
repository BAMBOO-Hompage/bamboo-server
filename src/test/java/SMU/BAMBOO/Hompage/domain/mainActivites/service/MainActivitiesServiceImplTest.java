package SMU.BAMBOO.Hompage.domain.mainActivites.service;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesResponseDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.entity.MainActivities;
import SMU.BAMBOO.Hompage.domain.mainActivites.repository.MainActivitiesRepository;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Service;
import SMU.BAMBOO.Hompage.mock.repository.FakeMainActivitiesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MainActivitiesServiceImplTest {

    private MainActivitiesRepository mainActivitiesRepository;
    private AwsS3Service awsS3Service;
    private MainActivitiesServiceImpl mainActivitiesService;
    private Member testMember;

    @BeforeEach
    void setUp() {
        mainActivitiesRepository = new FakeMainActivitiesRepository();
        awsS3Service = Mockito.mock(AwsS3Service.class);
        mainActivitiesService = new MainActivitiesServiceImpl(mainActivitiesRepository, awsS3Service);

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
    void create_성공_테스트() {
        // Given
        MainActivitiesRequestDTO.Create request = new MainActivitiesRequestDTO.Create();
        request.setTitle("BAMBOO 프로젝트 개발");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 2, 28));
        request.setYear(2025);

        List<String> imageUrls = List.of("https://s3.aws.com/image1.png");

        // When
        MainActivitiesResponseDTO.Detail response = mainActivitiesService.create(request, imageUrls, testMember);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("BAMBOO 프로젝트 개발");
        assertThat(response.getYear()).isEqualTo(2025);
        assertThat(response.getImages()).contains("https://s3.aws.com/image1.png");
    }

    @Test
    void getMainActivity_조회_성공() {
        // Given
        MainActivitiesRequestDTO.Create request = new MainActivitiesRequestDTO.Create();
        request.setTitle("BAMBOO 프로젝트");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 2, 28));
        request.setYear(2025);
        List<String> images = List.of("https://s3.aws.com/image1.png");

        MainActivitiesResponseDTO.Detail savedActivity = mainActivitiesService.create(request, images, testMember);

        MainActivities mainActivityEntity = mainActivitiesRepository.findById(savedActivity.getMainActivitiesId())
                .orElseThrow(() -> new CustomException(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST));

        // When
        MainActivitiesResponseDTO.Detail response = mainActivitiesService.getMainActivity(mainActivityEntity.getMainActivitiesId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getMainActivitiesId()).isEqualTo(mainActivityEntity.getMainActivitiesId());
        assertThat(response.getTitle()).isEqualTo("BAMBOO 프로젝트");
        assertThat(response.getStartDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2025, 2, 28));
        assertThat(response.getYear()).isEqualTo(2025);
        assertThat(response.getImages()).containsExactlyElementsOf(images);
        assertThat(response.getMemberName()).isEqualTo(testMember.getName());
    }

    @Test
    void getMainActivity_존재하지_않는_경우_예외() {
        // When
        // Then
        assertThatThrownBy(() -> mainActivitiesService.getMainActivity(999L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST.getMessage());
    }

    @Test
    void getMainActivitiesByYear_테스트() {
        // Given
        int year = 2025;
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

        mainActivitiesService.create(request1, List.of(), testMember);
        mainActivitiesService.create(request2, List.of(), testMember);

        // When
        Page<MainActivitiesResponseDTO.ActivitiesByYearResponse> result = mainActivitiesService.getMainActivitiesByYear(year, 0, 10);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.getContent()).extracting("title").contains("BAMBOO 프로젝트 개발", "스터디 활동");
    }

    @Test
    void updateMainActivity_성공_테스트() {
        // Given
        MainActivitiesRequestDTO.Create request = new MainActivitiesRequestDTO.Create();
        request.setTitle("BAMBOO 프로젝트");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 2, 28));
        request.setYear(2025);
        List<String> images = List.of("https://s3.aws.com/old_image.png");

        MainActivitiesResponseDTO.Detail savedActivity = mainActivitiesService.create(request, images, testMember);

        when(awsS3Service.uploadFile(anyString(), any(), anyBoolean()))
                .thenReturn("https://s3.aws.com/new_image.png");

        MainActivitiesRequestDTO.Update updateRequest = new MainActivitiesRequestDTO.Update();
        updateRequest.setTitle("[수정] BAMBOO 프로젝트");
        updateRequest.setYear(2026);

        List<Object> updatedImages = List.of("https://s3.aws.com/old_image.png", new MockMultipartFile("file", "test.png", "image/png", new byte[10])); // 파일 업로드 테스트 용도 (실제 업로드X)

        // When
        mainActivitiesService.updateMainActivity(savedActivity.getMainActivitiesId(), updateRequest, updatedImages, testMember);
        MainActivitiesResponseDTO.Detail updatedActivity = mainActivitiesService.getMainActivity(savedActivity.getMainActivitiesId());

        // Then
        assertThat(updatedActivity.getTitle()).isEqualTo("[수정] BAMBOO 프로젝트");
        assertThat(updatedActivity.getYear()).isEqualTo(2026);
        assertThat(updatedActivity.getImages()).contains("https://s3.aws.com/old_image.png");
        assertThat(updatedActivity.getImages()).contains("https://s3.aws.com/new_image.png");
        verify(awsS3Service, times(1)).uploadFile(anyString(), any(), anyBoolean());
    }

    @Test
    void deleteMainActivity_테스트() {
        // Given
        MainActivitiesRequestDTO.Create request = new MainActivitiesRequestDTO.Create();
        request.setTitle("BAMBOO 프로젝트");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 2, 28));
        request.setYear(2025);
        List<String> images = List.of("https://s3.aws.com/image1.png");

        MainActivitiesResponseDTO.Detail savedActivity = mainActivitiesService.create(request, images, testMember);

        // When
        mainActivitiesService.deleteMainActivity(savedActivity.getMainActivitiesId(), testMember);

        // Then
        assertThat(mainActivitiesRepository.findById(savedActivity.getMainActivitiesId())).isEmpty();

        assertThatThrownBy(() -> mainActivitiesService.getMainActivity(savedActivity.getMainActivitiesId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST.getMessage());

        verify(awsS3Service, times(1)).deleteFile("https://s3.aws.com/image1.png");

        assertThatThrownBy(() -> mainActivitiesService.deleteMainActivity(savedActivity.getMainActivitiesId(), testMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST.getMessage());
    }
}