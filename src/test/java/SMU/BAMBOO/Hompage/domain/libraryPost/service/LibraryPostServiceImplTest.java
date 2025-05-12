package SMU.BAMBOO.Hompage.domain.libraryPost.service;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostRequestDTO;
import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostResponseDTO;
import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Facade;
import SMU.BAMBOO.Hompage.mock.repository.FakeLibraryPostRepository;
import SMU.BAMBOO.Hompage.mock.repository.FakeTagRepository;
import SMU.BAMBOO.Hompage.util.SecurityTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class LibraryPostServiceImplTest {

    private FakeLibraryPostRepository fakeLibraryPostRepository;
    private LibraryPostServiceImpl libraryPostService;
    private Member testMember;
    private MultipartFile file;

    @BeforeEach
    void init() {
        fakeLibraryPostRepository = new FakeLibraryPostRepository();
        FakeTagRepository fakeTagRepository = new FakeTagRepository();
        AwsS3Facade awsS3Facade = Mockito.mock(AwsS3Facade.class);
        when(awsS3Facade.uploadFile(anyString(), any(), anyBoolean()))
                .thenReturn("https://s3.aws.com/test.pdf");

        libraryPostService = LibraryPostServiceImpl.builder()
                .libraryPostRepository(fakeLibraryPostRepository)
                .tagRepository(fakeTagRepository)
                .awsS3Facade(awsS3Facade)
                .build();

        testMember = Member.builder()
                .memberId(1L)
                .studentId("202010766")
                .email("test1@naver.com")
                .pw("1234")
                .name("kim")
                .major("휴먼")
                .phone("010-1111-1111")
                .role(Role.ROLE_USER)
                .build();

        fakeTagRepository.save(Tag.builder().tagId(1L).name("CV").build());
        fakeTagRepository.save(Tag.builder().tagId(2L).name("ML").build());
        fakeTagRepository.save(Tag.builder().tagId(3L).name("AI").build());

        fakeLibraryPostRepository.save(LibraryPost.builder()
                .libraryPostId(1L)
                .member(testMember)
                .paperName("CV 논문1")
                .year("2025")
                .topic("CV의 ~에 대하여")
                .link("link..")
                .build());

        file = new MockMultipartFile(
                "files", "test-file.pdf", "application/pdf", "test-file-content".getBytes()
        );

        SecurityTestUtil.setAuthentication(testMember);
    }

    @Test
    void LibraryPost_생성_테스트() {
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
        LibraryPostResponseDTO.Create response = libraryPostService.create(request, testMember, file);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.paperName()).isEqualTo("CV 논문");
        assertThat(response.year()).isEqualTo("2025");
        assertThat(response.topic()).isEqualTo("CV의 ~에 대하여");
        assertThat(response.content()).isEqualTo("최신 논문에 대한 정리");
        assertThat(response.link()).isEqualTo("http://link.com");
        assertThat(response.tagNames()).hasSize(2).contains("CV", "ML");
    }

    @Test
    void 존재하지_않는_LibraryPost_조회_예외_테스트() {
        // When
        // Then
        assertThatThrownBy(() -> libraryPostService.getById(99L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.LIBRARY_POST_NOT_EXIST.getMessage());
    }

    @Test
    public void LibraryPost_수정_테스트(){
        //given
        LibraryPostRequestDTO.Update request = new LibraryPostRequestDTO.Update(
                "http://linkkk.com",
                "2024",
                "CV 논문2",
                "머신러닝의 ~에 대하여",
                "[수정] 최신 논문에 대한 정리",
                List.of("ML")
        );

        //when
        libraryPostService.update(1L, request, file);

        //then
        Optional<LibraryPost> updatedPost = fakeLibraryPostRepository.findById(1L);
        assertThat(updatedPost).isPresent();

        LibraryPostResponseDTO.GetOne response = LibraryPostResponseDTO.GetOne.from(updatedPost.get());

        assertThat(response).isNotNull();
        assertThat(response.link()).isEqualTo("http://linkkk.com");
        assertThat(response.year()).isEqualTo("2024");
        assertThat(response.paperName()).isEqualTo("CV 논문2");
        assertThat(response.topic()).isEqualTo("머신러닝의 ~에 대하여");
        assertThat(response.content()).isEqualTo("[수정] 최신 논문에 대한 정리");
        assertThat(response.tagNames()).hasSize(1).contains("ML");
    }

    @Test
    void 존재하지_않는_LibraryPost_수정_예외_테스트() {
        // Given
        LibraryPostRequestDTO.Update request = new LibraryPostRequestDTO.Update(
                "http://linkkk.com",
                "2024",
                "CV 논문2",
                "머신러닝의 ~에 대하여",
                "최신 논문에 대한 정리",
                List.of("ML")
        );

        // When
        // Then
        assertThatThrownBy(() -> libraryPostService.update(99L, request, file))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.LIBRARY_POST_NOT_EXIST.getMessage());
    }

    @Test
    void 알렉산드리아_글_태그_초기화_테스트() {
        // Given
        Tag mlTag = Tag.builder()
                .tagId(1L)
                .name("ML")
                .build();
        LibraryPostRequestDTO.ResetTag request = new LibraryPostRequestDTO.ResetTag(List.of(mlTag.getName()));

        // When
        LibraryPostResponseDTO.GetOne response = libraryPostService.resetTags(1L, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.tagNames()).hasSize(1).contains("ML");

        Optional<LibraryPost> updatedPost = fakeLibraryPostRepository.findById(1L);
        assertThat(updatedPost).isPresent();
        assertThat(updatedPost.get().getLibraryPostTags()).hasSize(1);
        assertThat(updatedPost.get().getLibraryPostTags().get(0).getTag().getName()).isEqualTo("ML");
    }

    @Test
    void LibraryPost_삭제_테스트() {
        // Given
        // When
        libraryPostService.delete(1L);

        // Then
        Optional<LibraryPost> deletedPost = fakeLibraryPostRepository.findById(1L);
        assertThat(deletedPost).isEmpty();
    }

    @Test
    void 존재하지_않는_LibraryPost_삭제_예외_테스트() {
        // When & Then
        assertThatThrownBy(() -> libraryPostService.delete(99L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.LIBRARY_POST_NOT_EXIST.getMessage());
    }

}