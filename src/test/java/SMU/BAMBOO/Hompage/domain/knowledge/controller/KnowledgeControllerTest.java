package SMU.BAMBOO.Hompage.domain.knowledge.controller;

import SMU.BAMBOO.Hompage.domain.enums.KnowledgeType;
import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeRequestDTO;
import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
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

class KnowledgeControllerTest {

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
    @DisplayName("지식 공유 게시물 생성 성공")
    void createKnowledgeSuccessfully() {
        // Given
        KnowledgeRequestDTO.Create request = new KnowledgeRequestDTO.Create();
        request.setTitle("지식 공유 글");
        request.setContent("이 글은 지식 공유를 위한 글입니다.");
        request.setType(KnowledgeType.RESOURCES);

        MultipartFile imageFile = new MockMultipartFile(
                "images", "test-image.png", "image/png", "test-image-content".getBytes()
        );
        List<MultipartFile> images = List.of(imageFile);

        MultipartFile file = new MockMultipartFile(
                "files", "test-file.pdf", "application/pdf", "test-file-content".getBytes()
        );
        List<MultipartFile> files = List.of(file);

        when(testContainer.awsS3Service.uploadFiles(eq("knowledge/images"), anyList(), eq(true)))
                .thenReturn(List.of("https://s3.aws.com/image1.png"));

        when(testContainer.awsS3Service.uploadFiles(eq("knowledge/files"), anyList(), eq(false)))
                .thenReturn(List.of("https://s3.aws.com/file1.pdf"));

        // When
        SuccessResponse<KnowledgeResponseDTO.Create> response =
                testContainer.knowledgeController.createKnowledge(request, images, files, testMember);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getResult().title()).isEqualTo("지식 공유 글");
        assertThat(response.getResult().content()).isEqualTo("이 글은 지식 공유를 위한 글입니다.");
        assertThat(response.getResult().type()).isEqualTo(KnowledgeType.RESOURCES);
        assertThat(response.getResult().images()).containsExactly("https://s3.aws.com/image1.png");
        assertThat(response.getResult().files()).containsExactly("https://s3.aws.com/file1.pdf");

        assertThat(response.getResult().member()).isNotNull();
        assertThat(response.getResult().member().getName()).isEqualTo(testMember.getName());
        assertThat(response.getResult().member().getEmail()).isEqualTo(testMember.getEmail());
        assertThat(response.getResult().member().getRole()).isEqualTo(Role.ROLE_ADMIN);

        verify(testContainer.awsS3Service, times(1)).uploadFiles(eq("knowledge/images"), anyList(), anyBoolean());
        verify(testContainer.awsS3Service, times(1)).uploadFiles(eq("knowledge/files"), anyList(), anyBoolean());
    }

    @Test
    @DisplayName("지식 공유 게시물 단일 조회 성공")
    void getKnowledgeSuccessfully() {
        // Given
        KnowledgeRequestDTO.Create request = new KnowledgeRequestDTO.Create();
        request.setTitle("지식 공유 글");
        request.setContent("이 글은 지식 공유를 위한 글입니다.");
        request.setType(KnowledgeType.RESOURCES);

        SuccessResponse<KnowledgeResponseDTO.Create> createdResponse =
                testContainer.knowledgeController.createKnowledge(request, new ArrayList<>(), new ArrayList<>(), testMember);

        Long knowledgeId = createdResponse.getResult().knowledgeId();

        // When
        SuccessResponse<KnowledgeResponseDTO.GetOne> response =
                testContainer.knowledgeController.getKnowledge(knowledgeId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getResult().title()).isEqualTo("지식 공유 글");
        assertThat(response.getResult().content()).isEqualTo("이 글은 지식 공유를 위한 글입니다.");
    }

    @Test
    @DisplayName("지식 공유 게시물 삭제 성공")
    void deleteKnowledgeSuccessfully() {
        // Given
        KnowledgeRequestDTO.Create request = new KnowledgeRequestDTO.Create();
        request.setTitle("지식 공유 글");
        request.setContent("이 글은 지식 공유를 위한 글입니다.");
        request.setType(KnowledgeType.RESOURCES);

        SuccessResponse<KnowledgeResponseDTO.Create> createdResponse =
                testContainer.knowledgeController.createKnowledge(request, new ArrayList<>(), new ArrayList<>(), testMember);

        Long knowledgeId = createdResponse.getResult().knowledgeId();

        // When
        testContainer.knowledgeController.deleteKnowledge(knowledgeId);

        // Then
        assertThatThrownBy(() -> testContainer.knowledgeController.getKnowledge(knowledgeId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.KNOWLEDGE_NOT_EXIST.getMessage());
    }

    @Test
    @DisplayName("지식 공유 게시물 수정 성공")
    void updateKnowledgeSuccessfully() {
        // Given
        KnowledgeRequestDTO.Create request = new KnowledgeRequestDTO.Create();
        request.setTitle("기존 지식 글");
        request.setContent("기존 내용");
        request.setType(KnowledgeType.RESOURCES);

        SuccessResponse<KnowledgeResponseDTO.Create> createdResponse =
                testContainer.knowledgeController.createKnowledge(request, new ArrayList<>(), new ArrayList<>(), testMember);

        Long knowledgeId = createdResponse.getResult().knowledgeId();

        KnowledgeRequestDTO.Update updateRequest = new KnowledgeRequestDTO.Update();
        updateRequest.setTitle("수정된 지식 글");
        updateRequest.setContent("수정된 내용");
        updateRequest.setType(KnowledgeType.CAREERS);

        List<String> existingImages = List.of("https://s3.aws.com/old_image.png");
        List<String> existingFiles = List.of("https://s3.aws.com/old_file.pdf");

        MultipartFile newImageFile = new MockMultipartFile(
                "newImages", "test-image.png", "image/png", "test-image-content".getBytes()
        );
        List<MultipartFile> newImages = List.of(newImageFile);

        MultipartFile newFile = new MockMultipartFile(
                "newFiles", "test-file.pdf", "application/pdf", "test-file-content".getBytes()
        );
        List<MultipartFile> newFiles = List.of(newFile);

        when(testContainer.awsS3Service.uploadFiles(eq("knowledge/images"), anyList(), eq(true)))
                .thenReturn(List.of("https://s3.aws.com/new_image.png"));

        when(testContainer.awsS3Service.uploadFiles(eq("knowledge/files"), anyList(), eq(false)))
                .thenReturn(List.of("https://s3.aws.com/new_file.pdf"));

        // When
        SuccessResponse<KnowledgeResponseDTO.Update> response =
                testContainer.knowledgeController.updateKnowledge(knowledgeId, updateRequest, existingImages, newImages, existingFiles, newFiles);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getResult().title()).isEqualTo("수정된 지식 글");
        assertThat(response.getResult().content()).isEqualTo("수정된 내용");
        assertThat(response.getResult().type()).isEqualTo(KnowledgeType.CAREERS);
        assertThat(response.getResult().images()).containsExactlyInAnyOrder(
                "https://s3.aws.com/new_image.png", "https://s3.aws.com/old_image.png"
        );
        assertThat(response.getResult().files()).containsExactlyInAnyOrder(
                "https://s3.aws.com/old_file.pdf", "https://s3.aws.com/new_file.pdf"
        );

        verify(testContainer.awsS3Service, times(1)).uploadFiles(eq("knowledge/images"), anyList(), eq(true));
        verify(testContainer.awsS3Service, times(1)).uploadFiles(eq("knowledge/files"), anyList(), eq(false));
    }
}