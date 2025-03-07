package SMU.BAMBOO.Hompage.domain.knowledge.service;

import SMU.BAMBOO.Hompage.domain.enums.KnowledgeType;
import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeRequestDTO;
import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.mock.container.TestContainer;
import SMU.BAMBOO.Hompage.util.SecurityTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class KnowledgeServiceImplTest {

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

        SecurityTestUtil.setAuthentication(testMember);
    }

    @Test
    @DisplayName("지식 공유 글 생성 성공")
    void createKnowledgeSuccessfully() {
        // Given
        KnowledgeRequestDTO.Create request = new KnowledgeRequestDTO.Create();
        request.setTitle("지식 공유 글");
        request.setContent("이 글은 지식 공유를 위한 글입니다.");
        request.setType(KnowledgeType.RESOURCES);

        List<MultipartFile> images = List.of(
                new MockMultipartFile("images", "test-image.png", "image/png", "test-image-content".getBytes())
        );
        List<MultipartFile> files = List.of(
                new MockMultipartFile("files", "test-file.pdf", "application/pdf", "test-file-content".getBytes())
        );

        when(testContainer.awsS3Service.uploadFiles(eq("knowledge/images"), anyList(), eq(true)))
                .thenReturn(List.of("https://s3.aws.com/image1.png"));

        when(testContainer.awsS3Service.uploadFiles(eq("knowledge/files"), anyList(), eq(false)))
                .thenReturn(List.of("https://s3.aws.com/file1.pdf"));

        // When
        KnowledgeResponseDTO.Create response = testContainer.knowledgeService.create(request, testMember, images, files);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("지식 공유 글");
        assertThat(response.content()).isEqualTo("이 글은 지식 공유를 위한 글입니다.");
        assertThat(response.type()).isEqualTo(KnowledgeType.RESOURCES);
        assertThat(response.images()).containsExactly("https://s3.aws.com/image1.png");
        assertThat(response.files()).containsExactly("https://s3.aws.com/file1.pdf");
    }

    @Test
    @DisplayName("지식 공유 글 단일 조회 성공")
    void getKnowledgeByIdSuccessfully() {
        // Given
        KnowledgeRequestDTO.Create request = new KnowledgeRequestDTO.Create();
        request.setTitle("지식 공유 글");
        request.setContent("이 글은 지식 공유를 위한 글입니다.");
        request.setType(KnowledgeType.RESOURCES);

        KnowledgeResponseDTO.Create createdResponse =
                testContainer.knowledgeService.create(request, testMember, new ArrayList<>(), new ArrayList<>());

        Long knowledgeId = createdResponse.knowledgeId();

        // When
        KnowledgeResponseDTO.GetOne response = testContainer.knowledgeService.getById(knowledgeId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("지식 공유 글");
        assertThat(response.content()).isEqualTo("이 글은 지식 공유를 위한 글입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 지식 공유 글 조회 시 예외 발생")
    void getKnowledgeById_NotExist_Fail() {
        // When
        // Then
        assertThatThrownBy(() -> testContainer.knowledgeService.getById(999L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.KNOWLEDGE_NOT_EXIST.getMessage());
    }

    @Test
    @DisplayName("지식 공유 글 삭제 성공")
    void deleteKnowledgeSuccessfully() {
        // Given
        KnowledgeRequestDTO.Create request = new KnowledgeRequestDTO.Create();
        request.setTitle("지식 공유 글");
        request.setContent("이 글은 지식 공유를 위한 글입니다.");
        request.setType(KnowledgeType.RESOURCES);

        KnowledgeResponseDTO.Create createdResponse =
                testContainer.knowledgeService.create(request, testMember, new ArrayList<>(), new ArrayList<>());

        Long knowledgeId = createdResponse.knowledgeId();

        // When
        testContainer.knowledgeService.delete(testMember, knowledgeId);

        // Then
        assertThatThrownBy(() -> testContainer.knowledgeService.getById(knowledgeId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.KNOWLEDGE_NOT_EXIST.getMessage());
    }

    @Test
    @DisplayName("지식 공유 글 수정 성공")
    void updateKnowledgeSuccessfully() {
        // Given
        KnowledgeRequestDTO.Create request = new KnowledgeRequestDTO.Create();
        request.setTitle("기존 지식 글");
        request.setContent("기존 내용");
        request.setType(KnowledgeType.RESOURCES);

        KnowledgeResponseDTO.Create createdResponse =
                testContainer.knowledgeService.create(request, testMember, new ArrayList<>(), new ArrayList<>());

        Long knowledgeId = createdResponse.knowledgeId();

        KnowledgeRequestDTO.Update updateRequest = new KnowledgeRequestDTO.Update();
        updateRequest.setTitle("수정된 지식 글");
        updateRequest.setContent("수정된 내용");
        updateRequest.setType(KnowledgeType.RESOURCES);

        List<String> existingImages = List.of("https://s3.aws.com/old_image.png");
        List<String> existingFiles = List.of("https://s3.aws.com/old_file.pdf");

        List<MultipartFile> newImages = List.of(new MockMultipartFile(
                "newImages", "test-image.png", "image/png", "test-image-content".getBytes()));

        List<MultipartFile> newFiles = List.of(new MockMultipartFile(
                "newFiles", "test-file.pdf", "application/pdf", "test-file-content".getBytes()));

        when(testContainer.awsS3Service.uploadFiles(eq("knowledge/images"), anyList(), eq(true)))
                .thenReturn(List.of("https://s3.aws.com/new_image.png"));

        when(testContainer.awsS3Service.uploadFiles(eq("knowledge/files"), anyList(), eq(false)))
                .thenReturn(List.of("https://s3.aws.com/new_file.pdf"));

        // When
        KnowledgeResponseDTO.Update response =
                testContainer.knowledgeService.update(testMember, knowledgeId, updateRequest, existingImages, newImages, existingFiles, newFiles);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("수정된 지식 글");
        assertThat(response.content()).isEqualTo("수정된 내용");
        assertThat(response.type()).isEqualTo(KnowledgeType.RESOURCES);
        assertThat(response.images()).containsExactlyInAnyOrder(
                "https://s3.aws.com/new_image.png", "https://s3.aws.com/old_image.png"
        );
        assertThat(response.files()).containsExactlyInAnyOrder(
                "https://s3.aws.com/old_file.pdf", "https://s3.aws.com/new_file.pdf"
        );

        verify(testContainer.awsS3Service, times(1)).uploadFiles(eq("knowledge/images"), anyList(), eq(true));
        verify(testContainer.awsS3Service, times(1)).uploadFiles(eq("knowledge/files"), anyList(), eq(false));
    }
}