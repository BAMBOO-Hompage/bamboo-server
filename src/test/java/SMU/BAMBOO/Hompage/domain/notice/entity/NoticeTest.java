package SMU.BAMBOO.Hompage.domain.notice.entity;

import SMU.BAMBOO.Hompage.domain.enums.NoticeType;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeTest {

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .memberId(1L)
                .email("kjk@example.com")
                .name("김재관")
                .build();
    }

    @Test
    @DisplayName("공지사항 객체 생성 테스트")
    void createNoticeSuccessfully() {
        // Given
        NoticeRequestDTO.Create request = new NoticeRequestDTO.Create();
        request.setTitle("공지사항1");
        request.setContent("공지사항1 내용");
        request.setType(NoticeType.NOTICE);

        List<String> imageUrls = List.of("https://s3.aws.com/image1.png", "https://s3.aws.com/image2.png");
        List<String> fileUrls = List.of("https://s3.aws.com/file1.pdf", "https://s3.aws.com/file2.pdf");

        // When
        Notice notice = Notice.from(request, testMember, imageUrls, fileUrls);

        // Then
        assertThat(notice).isNotNull();
        assertThat(notice.getMember()).isEqualTo(testMember);
        assertThat(notice.getTitle()).isEqualTo("공지사항1");
        assertThat(notice.getContent()).isEqualTo("공지사항1 내용");
        assertThat(notice.getType()).isEqualTo(NoticeType.NOTICE);
        assertThat(notice.getImages()).containsExactly("https://s3.aws.com/image1.png", "https://s3.aws.com/image2.png");
        assertThat(notice.getFiles()).containsExactly("https://s3.aws.com/file1.pdf", "https://s3.aws.com/file2.pdf");
    }

    @Test
    @DisplayName("공지사항 업데이트 테스트")
    void updateNoticeSuccessfully() {
        // Given
        Notice notice = Notice.builder()
                .member(testMember)
                .title("공지사항1")
                .content("공지사항1 내용")
                .type(NoticeType.NOTICE)
                .images(new ArrayList<>(List.of("https://s3.aws.com/old_image.png")))
                .files(new ArrayList<>(List.of("https://s3.aws.com/old_file.pdf")))
                .build();

        NoticeRequestDTO.Update updateRequest = new NoticeRequestDTO.Update();
        updateRequest.setTitle("[수정] 공지사항1");
        updateRequest.setContent("수정된 내용");
        updateRequest.setType(NoticeType.EVENTS);

        List<String> updatedImages = List.of("https://s3.aws.com/new_image.png");
        List<String> updatedFiles = List.of("https://s3.aws.com/new_file.pdf");

        // When
        notice.update(updateRequest, updatedImages, updatedFiles);

        // Then
        assertThat(notice.getTitle()).isEqualTo("[수정] 공지사항1");
        assertThat(notice.getContent()).isEqualTo("수정된 내용");
        assertThat(notice.getType()).isEqualTo(NoticeType.EVENTS);
        assertThat(notice.getImages()).containsExactly("https://s3.aws.com/new_image.png");
        assertThat(notice.getFiles()).containsExactly("https://s3.aws.com/new_file.pdf");
    }

    @Test
    @DisplayName("공지사항 업데이트 시 빈 리스트가 들어올 경우 테스트")
    void updateNoticeWithNullImageAndFileLists() {
        // Given
        Notice notice = Notice.builder()
                .member(testMember)
                .title("공지사항1")
                .content("공지사항1 내용")
                .type(NoticeType.NOTICE)
                .images(new ArrayList<>())
                .files(new ArrayList<>())
                .build();

        NoticeRequestDTO.Update updateRequest = new NoticeRequestDTO.Update();
        updateRequest.setTitle("[수정] 공지사항1");
        updateRequest.setContent("수정된 내용");
        updateRequest.setType(NoticeType.EVENTS);

        List<String> updatedImages = List.of("https://s3.aws.com/new_image.png");
        List<String> updatedFiles = List.of("https://s3.aws.com/new_file.pdf");

        // When
        notice.update(updateRequest, updatedImages, updatedFiles);

        // Then
        assertThat(notice.getImages()).isNotNull();
        assertThat(notice.getImages()).containsExactly("https://s3.aws.com/new_image.png");

        assertThat(notice.getFiles()).isNotNull();
        assertThat(notice.getFiles()).containsExactly("https://s3.aws.com/new_file.pdf");
    }
}