package SMU.BAMBOO.Hompage.domain.libraryPost.controller;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostRequestDTO;
import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Facade;
import SMU.BAMBOO.Hompage.mock.container.TestContainer;
import SMU.BAMBOO.Hompage.util.SecurityTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class LibraryPostControllerTest {

    private TestContainer testContainer;
    private Member testMember;

    @BeforeEach
    void setUp() {
        testContainer = new TestContainer();
        AwsS3Facade awsS3Facade = Mockito.mock(AwsS3Facade.class);

        testMember = Member.builder()
                .memberId(1L)
                .studentId("202010766")
                .email("test1@naver.com")
                .pw("1234")
                .name("김재관")
                .major("휴먼")
                .phone("010-1111-1111")
                .role(Role.ROLE_USER)
                .build();

        SecurityTestUtil.setAuthentication(testMember);

        testContainer.tagRepository.save(Tag.builder().tagId(1L).name("CV").build());
        testContainer.tagRepository.save(Tag.builder().tagId(2L).name("ML").build());
        testContainer.tagRepository.save(Tag.builder().tagId(3L).name("DA").build());
        testContainer.tagRepository.save(Tag.builder().tagId(4L).name("DL").build());

        when(awsS3Facade.uploadFile(anyString(), any(), anyBoolean()))
                .thenReturn("https://s3.aws.com/test.pdf");
    }

    @Test
    void LibraryPost_생성_Controller_테스트() {
        // Given
        LibraryPostRequestDTO.Create request = new LibraryPostRequestDTO.Create(
                "http://link.com",
                "2025",
                "CV 논문",
                "CV의 ~에 대하여",
                "최신 논문에 대한 정리",
                List.of("CV", "ML")
        );

        MultipartFile file = new MockMultipartFile(
                "files", "test-file.pdf", "application/pdf", "test-file-content".getBytes()
        );

        // When
        LibraryPostResponseDTO.Create response =
                testContainer.libraryPostService.create(request, testMember, file);

        // Then
        assertThat(response.link()).isEqualTo("http://link.com");
        assertThat(response.year()).isEqualTo("2025");
        assertThat(response.member().name()).isEqualTo("김재관");
        assertThat(response.paperName()).isEqualTo("CV 논문");
        assertThat(response.topic()).isEqualTo("CV의 ~에 대하여");
        assertThat(response.content()).isEqualTo("최신 논문에 대한 정리");
        assertThat(response.tagNames()).isEqualTo(List.of("CV", "ML"));
    }

    @Test
    void LibraryPost_조회_Controller_테스트() {
        // Given
        LibraryPostRequestDTO.Create request = new LibraryPostRequestDTO.Create(
                "http://link.com",
                "2025",
                "CV 논문",
                "CV의 ~에 대하여",
                "최신 논문에 대한 정리",
                List.of("CV", "ML")
        );
        MultipartFile file = new MockMultipartFile(
                "files", "test-file.pdf", "application/pdf", "test-file-content".getBytes()
        );
        LibraryPostResponseDTO.Create libraryPost = testContainer.libraryPostService.create(request, testMember, file);

        // When
        LibraryPostResponseDTO.GetOne response = testContainer.libraryPostService.getById(libraryPost.libraryPostId());

        // Then
        assertThat(response.link()).isEqualTo("http://link.com");
        assertThat(response.year()).isEqualTo("2025");
        assertThat(response.paperName()).isEqualTo("CV 논문");
        assertThat(response.topic()).isEqualTo("CV의 ~에 대하여");
        assertThat(response.content()).isEqualTo("최신 논문에 대한 정리");
        assertThat(response.tagNames()).isEqualTo(List.of("CV", "ML"));
    }

    @Test
    void LibraryPost_수정_Controller_테스트() {
        // Given
        LibraryPostRequestDTO.Create request = new LibraryPostRequestDTO.Create(
                "http://link.com",
                "2025",
                "CV 논문",
                "CV의 ~에 대하여",
                "최신 논문에 대한 정리",
                List.of("CV", "ML")
        );
        MultipartFile file = new MockMultipartFile(
                "files", "test-file.pdf", "application/pdf", "test-file-content".getBytes()
        );
        LibraryPostResponseDTO.Create libraryPost = testContainer.libraryPostService.create(request, testMember, file);

        LibraryPostRequestDTO.Update updateRequest = new LibraryPostRequestDTO.Update(
                "http://newlink.com",
                "2020",
                "DL 논문",
                "DL의 ~에 대하여",
                "최신 딥러닝 논문에 대한 정리",
                List.of("DA", "DL"),
                null
        );

        // When
        testContainer.libraryPostService.update(libraryPost.libraryPostId(), updateRequest, file);
        LibraryPostResponseDTO.GetOne updatedPost = testContainer.libraryPostService.getById(libraryPost.libraryPostId());

        // Then
        assertThat(updatedPost.link()).isEqualTo("http://newlink.com");
        assertThat(updatedPost.year()).isEqualTo("2020");
        assertThat(updatedPost.paperName()).isEqualTo("DL 논문");
        assertThat(updatedPost.topic()).isEqualTo("DL의 ~에 대하여");
        assertThat(updatedPost.content()).isEqualTo("최신 딥러닝 논문에 대한 정리");
        assertThat(updatedPost.tagNames()).isEqualTo(List.of("DA", "DL"));
    }

    @Test
    void LibraryPost_삭제_Controller_테스트() {
        // Given
        LibraryPostRequestDTO.Create request = new LibraryPostRequestDTO.Create(
                "http://link.com",
                "2025",
                "CV 논문",
                "CV의 ~에 대하여",
                "최신 논문에 대한 정리",
                List.of("CV", "ML")
        );
        MultipartFile file = new MockMultipartFile(
                "files", "test-file.pdf", "application/pdf", "test-file-content".getBytes()
        );
        LibraryPostResponseDTO.Create createdPost = testContainer.libraryPostService.create(request, testMember, file);

        // When
        testContainer.libraryPostService.delete(createdPost.libraryPostId());

        // Then
        assertThrows(CustomException.class, () -> testContainer.libraryPostService.getById(createdPost.libraryPostId()));
    }

    @Test
    void LibraryPost_태그_초기화_Controller_테스트() {
        // Given
        LibraryPostRequestDTO.Create request = new LibraryPostRequestDTO.Create(
                "http://link.com",
                "2025",
                "CV 논문",
                "CV의 ~에 대하여",
                "최신 논문에 대한 정리",
                List.of("CV", "ML")
        );
        MultipartFile file = new MockMultipartFile(
                "files", "test-file.pdf", "application/pdf", "test-file-content".getBytes()
        );
        LibraryPostResponseDTO.Create createdPost = testContainer.libraryPostService.create(request, testMember, file);
        LibraryPostRequestDTO.ResetTag resetTagRequest = new LibraryPostRequestDTO.ResetTag(List.of("DA", "DL"));

        // When
        LibraryPostResponseDTO.GetOne updatedPost = testContainer.libraryPostService.resetTags(createdPost.libraryPostId(), resetTagRequest);

        // Then
        assertThat(updatedPost.tagNames()).isEqualTo(List.of("DA", "DL"));
    }
}
