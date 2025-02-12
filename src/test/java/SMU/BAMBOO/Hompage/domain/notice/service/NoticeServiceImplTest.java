package SMU.BAMBOO.Hompage.domain.notice.service;

import SMU.BAMBOO.Hompage.domain.enums.NoticeType;
import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeRequestDTO;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeResponseDTO;
import SMU.BAMBOO.Hompage.domain.notice.entity.Notice;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Service;
import SMU.BAMBOO.Hompage.mock.repository.FakeNoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NoticeServiceImplTest {

    private FakeNoticeRepository noticeRepository;
    private AwsS3Service awsS3Service;
    private NoticeServiceImpl noticeService;
    private Member adminMember;
    private Member userMember;

    @BeforeEach
    void setUp() {
        noticeRepository = new FakeNoticeRepository();
        awsS3Service = mock(AwsS3Service.class);
        noticeService = new NoticeServiceImpl(noticeRepository, awsS3Service);

        adminMember = Member.builder()
                .memberId(1L)
                .studentId("202010766")
                .email("kjk1@example.com")
                .name("김재관_운영진")
                .major("휴먼지능정보공학과")
                .role(Role.ROLE_ADMIN)
                .build();

        userMember = Member.builder()
                .memberId(2L)
                .studentId("202010767")
                .email("kjk2@example.com")
                .name("김재관_회원")
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

        List<MultipartFile> images = List.of(mock(MultipartFile.class));
        List<MultipartFile> files = List.of(mock(MultipartFile.class));

        when(awsS3Service.uploadFiles(anyString(), anyList(), anyBoolean()))
                .thenReturn(List.of("https://s3.aws.com/image1.png"));

        // When
        NoticeResponseDTO.Detail response = noticeService.create(request, adminMember, images, files);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("공지1");
        assertThat(response.content()).isEqualTo("공지1 내용");
        assertThat(response.type()).isEqualTo(NoticeType.NOTICE);
        assertThat(response.images()).contains("https://s3.aws.com/image1.png");
    }

    @Test
    @DisplayName("공지사항 단일 조회 성공")
    void getNoticeSuccessfully() {
        // Given
        NoticeRequestDTO.Create request = new NoticeRequestDTO.Create();
        request.setTitle("공지1");
        request.setContent("공지1 내용");
        request.setType(NoticeType.NOTICE);

        Notice notice = Notice.from(request, adminMember, List.of("https://s3.aws.com/image.png"), new ArrayList<>());

        Notice savedNotice = noticeRepository.save(notice);
        Long savedNoticeId = savedNotice.getNoticeId();

        // When
        NoticeResponseDTO.Detail response = noticeService.getNotice(savedNoticeId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.member().getName()).isEqualTo("김재관_운영진");
        assertThat(response.title()).isEqualTo("공지1");
        assertThat(response.content()).isEqualTo("공지1 내용");
        assertThat(response.type()).isEqualTo(NoticeType.NOTICE);
        assertThat(response.images()).contains("https://s3.aws.com/image.png");
        assertThat(response.files()).isNotNull();
        assertThat(response.files()).isEmpty();
    }

    @Test
    @DisplayName("공지사항 삭제 성공")
    void deleteNoticeSuccessfully() {
        // Given
        NoticeRequestDTO.Create request = new NoticeRequestDTO.Create();
        request.setTitle("공지1");
        request.setContent("공지1 내용");
        request.setType(NoticeType.NOTICE);

        Notice notice = Notice.from(request, adminMember, List.of("https://s3.aws.com/image.png"), new ArrayList<>());

        Notice savedNotice = noticeRepository.save(notice);
        Long savedNoticeId = savedNotice.getNoticeId();

        // When
        noticeService.deleteNotice(savedNoticeId, adminMember);

        // Then
        assertThatThrownBy(() -> noticeService.getNotice(notice.getNoticeId()))
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

        Notice notice = Notice.from(request, adminMember, List.of("https://s3.aws.com/image.png"), new ArrayList<>());

        Notice savedNotice = noticeRepository.save(notice);
        Long savedNoticeId = savedNotice.getNoticeId();

        NoticeRequestDTO.Update updateRequest = new NoticeRequestDTO.Update();
        updateRequest.setTitle("[수정] 공지1");
        updateRequest.setContent("수정된 내용");
        updateRequest.setType(NoticeType.EVENTS);

        List<String> existingImages = List.of("https://s3.aws.com/old_image.png");
        List<String> existingFiles = List.of("https://s3.aws.com/old_file.pdf");

        List<MultipartFile> newImages = List.of(mock(MultipartFile.class));
        List<MultipartFile> newFiles = List.of(mock(MultipartFile.class));

        when(awsS3Service.uploadFiles(eq("notice/images"), anyList(), eq(true)))
                .thenReturn(List.of("https://s3.aws.com/new_image.png"));

        when(awsS3Service.uploadFiles(eq("notice/files"), anyList(), eq(false)))
                .thenReturn(List.of("https://s3.aws.com/new_file.pdf"));

        // When
        NoticeResponseDTO.Detail response = noticeService.update(savedNoticeId, updateRequest, existingImages, newImages, existingFiles, newFiles);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.member()).isNotNull();
        assertThat(response.member().getName()).isEqualTo(adminMember.getName());
        assertThat(response.title()).isEqualTo("[수정] 공지1");
        assertThat(response.content()).isEqualTo("수정된 내용");
        assertThat(response.type()).isEqualTo(NoticeType.EVENTS);

        assertThat(response.images()).containsExactlyInAnyOrder(
                "https://s3.aws.com/new_image.png",
                "https://s3.aws.com/old_image.png"
        );

        assertThat(response.files()).containsExactlyInAnyOrder(
                "https://s3.aws.com/new_file.pdf",
                "https://s3.aws.com/old_file.pdf"
        );
    }

}