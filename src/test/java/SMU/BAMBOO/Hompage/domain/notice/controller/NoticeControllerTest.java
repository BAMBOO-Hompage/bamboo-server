package SMU.BAMBOO.Hompage.domain.notice.controller;

import SMU.BAMBOO.Hompage.domain.enums.NoticeType;
import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeRequestDTO;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeResponseDTO;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.mock.container.TestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class NoticeControllerTest {

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
    @DisplayName("공지사항 생성 성공")
    void createNoticeSuccessfully() {
        // Given
        NoticeRequestDTO.Create request = new NoticeRequestDTO.Create();
        request.setTitle("공지1");
        request.setContent("공지1 내용");
        request.setType(NoticeType.NOTICE);

        MockMultipartFile imageFile = new MockMultipartFile(
                "images",
                "test-image.png",
                "image/png",
                "test-image-content".getBytes()
        );

        List<MultipartFile> images = List.of(imageFile);

        when(testContainer.awsS3Service.uploadFiles(anyString(), anyList(), anyBoolean()))
                .thenReturn(List.of("https://s3.aws.com/image1.png"));

        // When
        SuccessResponse<NoticeResponseDTO.Detail> response =
                testContainer.noticeController.createNotice(request, images, new ArrayList<>(), testMember);

        // Then
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().title()).isEqualTo("공지1");
        assertThat(response.getResult().content()).isEqualTo("공지1 내용");
        assertThat(response.getResult().type()).isEqualTo(NoticeType.NOTICE);
        assertThat(response.getResult().images()).containsExactly("https://s3.aws.com/image1.png");
        assertThat(response.getResult().files()).isEmpty();

        assertThat(response.getResult().member()).isNotNull();
        assertThat(response.getResult().member().getName()).isEqualTo(testMember.getName());
        assertThat(response.getResult().member().getEmail()).isEqualTo(testMember.getEmail());
        assertThat(response.getResult().member().getRole()).isEqualTo(Role.ROLE_ADMIN);

        verify(testContainer.awsS3Service, times(1)).uploadFiles(anyString(), anyList(), anyBoolean());
    }

    @Test
    @DisplayName("공지사항 단일 조회 성공")
    void getNoticeSuccessfully() {
        // Given
        NoticeRequestDTO.Create request = new NoticeRequestDTO.Create();
        request.setTitle("공지1");
        request.setContent("공지1 내용");
        request.setType(NoticeType.NOTICE);

        SuccessResponse<NoticeResponseDTO.Detail> createdResponse =
                testContainer.noticeController.createNotice(request, new ArrayList<>(), new ArrayList<>(), testMember);

        Long noticeId = createdResponse.getResult().noticeId();

        // When
        SuccessResponse<NoticeResponseDTO.Detail> response =
                testContainer.noticeController.getMainActivity(noticeId);

        // Then
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().title()).isEqualTo("공지1");
        assertThat(response.getResult().content()).isEqualTo("공지1 내용");
        assertThat(response.getResult().type()).isEqualTo(NoticeType.NOTICE);
        assertThat(response.getResult().images()).isEmpty();
        assertThat(response.getResult().files()).isEmpty();

        assertThat(response.getResult().member()).isNotNull();
        assertThat(response.getResult().member().getName()).isEqualTo(testMember.getName());
        assertThat(response.getResult().member().getEmail()).isEqualTo(testMember.getEmail());
        assertThat(response.getResult().member().getRole()).isEqualTo(Role.ROLE_ADMIN);
    }

    @Test
    @DisplayName("존재하지 않는 공지사항 조회 시 예외 발생")
    void getNotice_NotExist_Fail() {
        // When
        // Then
        assertThatThrownBy(() -> testContainer.noticeController.getMainActivity(999L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOTICE_NOT_EXIST.getMessage());
    }

    @Test
    @DisplayName("공지사항 삭제 성공")
    void deleteNoticeSuccessfully() {
        // Given
        NoticeRequestDTO.Create request = new NoticeRequestDTO.Create();
        request.setTitle("공지1");
        request.setContent("공지1 내용");
        request.setType(NoticeType.NOTICE);

        SuccessResponse<NoticeResponseDTO.Detail> createdResponse =
                testContainer.noticeController.createNotice(request, new ArrayList<>(), new ArrayList<>(), testMember);

        Long noticeId = createdResponse.getResult().noticeId();

        // When
        testContainer.noticeController.deleteNotice(noticeId, testMember);

        // Then
        assertThatThrownBy(() -> testContainer.noticeController.getMainActivity(noticeId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOTICE_NOT_EXIST.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 공지사항 삭제 시 예외 발생")
    void deleteNotice_NotExist_Fail() {
        // When
        // Then
        assertThatThrownBy(() -> testContainer.noticeController.deleteNotice(999L, testMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOTICE_NOT_EXIST.getMessage());
    }


    @Test
    @DisplayName("공지사항 수정 성공")
    void updateNoticeSuccessfully() {
        // Given
        NoticeRequestDTO.Create request = new NoticeRequestDTO.Create();
        request.setTitle("공지1");
        request.setContent("공지1 내용");
        request.setType(NoticeType.NOTICE);

        SuccessResponse<NoticeResponseDTO.Detail> createdResponse =
                testContainer.noticeController.createNotice(request, new ArrayList<>(), new ArrayList<>(), testMember);

        Long noticeId = createdResponse.getResult().noticeId();

        NoticeRequestDTO.Update updateRequest = new NoticeRequestDTO.Update();
        updateRequest.setTitle("[수정] 공지1");
        updateRequest.setContent("수정된 내용");
        updateRequest.setType(NoticeType.EVENTS);

        List<String> existingImages = List.of("https://s3.aws.com/old_image.png");
        List<String> existingFiles = List.of("https://s3.aws.com/old_file.pdf");

        List<MultipartFile> newImages = List.of(new MockMultipartFile(
                "newImages", "test-image.png", "image/png", "test-image-content".getBytes()));

        List<MultipartFile> newFiles = List.of(new MockMultipartFile(
                "newFiles", "test-file.pdf", "application/pdf", "test-file-content".getBytes()));

        when(testContainer.awsS3Service.uploadFiles(eq("notice/images"), anyList(), eq(true)))
                .thenReturn(List.of("https://s3.aws.com/new_image.png"));

        when(testContainer.awsS3Service.uploadFiles(eq("notice/files"), anyList(), eq(false)))
                .thenReturn(List.of("https://s3.aws.com/new_file.pdf"));

        // When
        SuccessResponse<NoticeResponseDTO.Detail> response =
                testContainer.noticeController.updateNotice(noticeId, updateRequest, existingImages, newImages, existingFiles, newFiles);

        // Then
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().title()).isEqualTo("[수정] 공지1");
        assertThat(response.getResult().content()).isEqualTo("수정된 내용");
        assertThat(response.getResult().type()).isEqualTo(NoticeType.EVENTS);

        assertThat(response.getResult().images()).containsExactlyInAnyOrder(
                "https://s3.aws.com/new_image.png",
                "https://s3.aws.com/old_image.png"
        );

        assertThat(response.getResult().files()).containsExactlyInAnyOrder(
                "https://s3.aws.com/old_file.pdf",
                "https://s3.aws.com/new_file.pdf"
        );

        verify(testContainer.awsS3Service, times(1)).uploadFiles(eq("notice/images"), anyList(), eq(true));
        verify(testContainer.awsS3Service, times(1)).uploadFiles(eq("notice/files"), anyList(), eq(false));
    }

    @Test
    @DisplayName("존재하지 않는 공지사항 수정 시 예외 발생")
    void updateNotice_NotExist_Fail() {
        // Given
        NoticeRequestDTO.Update updateRequest = new NoticeRequestDTO.Update();
        updateRequest.setTitle("[수정] 공지1");
        updateRequest.setContent("수정된 내용");
        updateRequest.setType(NoticeType.EVENTS);

        // When
        // Then
        assertThatThrownBy(() -> testContainer.noticeController.updateNotice(999L, updateRequest,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOTICE_NOT_EXIST.getMessage());
    }

}
