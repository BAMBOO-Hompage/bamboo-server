package SMU.BAMBOO.Hompage.domain.knowledge.entity;

import SMU.BAMBOO.Hompage.domain.enums.KnowledgeType;
import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeRequestDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KnowledgeTest {

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
    @DisplayName("Knowledge 객체 생성 테스트")
    void createKnowledgeSuccessfully() {
        // Given
        KnowledgeRequestDTO.Create request = new KnowledgeRequestDTO.Create();
        request.setTitle("지식 공유 글");
        request.setContent("이 글은 지식 공유를 위한 글입니다.");
        request.setType(KnowledgeType.RESOURCES);

        List<String> imageUrls = List.of("https://s3.aws.com/image1.png", "https://s3.aws.com/image2.png");
        List<String> fileUrls = List.of("https://s3.aws.com/file1.pdf", "https://s3.aws.com/file2.pdf");

        // When
        Knowledge knowledge = Knowledge.from(request, testMember, imageUrls, fileUrls);

        // Then
        assertThat(knowledge).isNotNull();
        assertThat(knowledge.getMember()).isEqualTo(testMember);
        assertThat(knowledge.getTitle()).isEqualTo("지식 공유 글");
        assertThat(knowledge.getContent()).isEqualTo("이 글은 지식 공유를 위한 글입니다.");
        assertThat(knowledge.getType()).isEqualTo(KnowledgeType.RESOURCES);
        assertThat(knowledge.getViews()).isEqualTo(0);
        assertThat(knowledge.getImages()).containsExactly("https://s3.aws.com/image1.png", "https://s3.aws.com/image2.png");
        assertThat(knowledge.getFiles()).containsExactly("https://s3.aws.com/file1.pdf", "https://s3.aws.com/file2.pdf");
    }

    @Test
    @DisplayName("Knowledge 객체 수정 테스트")
    void updateKnowledgeSuccessfully() {
        // Given
        Knowledge knowledge = Knowledge.builder()
                .member(testMember)
                .title("파이썬 지식공유")
                .content("반복문이란 ~")
                .type(KnowledgeType.RESOURCES)
                .images(new ArrayList<>(List.of("https://s3.aws.com/old_image.png")))
                .files(new ArrayList<>(List.of("https://s3.aws.com/old_file.pdf")))
                .build();

        KnowledgeRequestDTO.Update updateRequest = new KnowledgeRequestDTO.Update();
        updateRequest.setTitle("[수정] 파이썬 지식공유");
        updateRequest.setContent("반복문이란 !!");
        updateRequest.setType(KnowledgeType.CAREERS);

        List<String> updatedImages = List.of("https://s3.aws.com/new_image.png");
        List<String> updatedFiles = List.of("https://s3.aws.com/new_file.pdf");

        // When
        knowledge.update(updateRequest, updatedImages, updatedFiles);

        // Then
        assertThat(knowledge.getTitle()).isEqualTo("[수정] 파이썬 지식공유");
        assertThat(knowledge.getContent()).isEqualTo("반복문이란 !!");
        assertThat(knowledge.getType()).isEqualTo(KnowledgeType.CAREERS);
        assertThat(knowledge.getImages()).containsExactly("https://s3.aws.com/new_image.png");
        assertThat(knowledge.getFiles()).containsExactly("https://s3.aws.com/new_file.pdf");
    }

    @Test
    @DisplayName("Knowledge 업데이트 시 이미지 및 파일 리스트가 null일 경우 테스트")
    void updateKnowledgeWithNullImageAndFileLists() {
        // Given
        Knowledge knowledge = Knowledge.builder()
                .member(testMember)
                .title("기존 지식 글")
                .content("기존 내용")
                .type(KnowledgeType.RESOURCES)
                .images(List.of("https://s3.aws.com/old_image.png"))
                .files(List.of("https://s3.aws.com/old_file.pdf"))
                .build();

        KnowledgeRequestDTO.Update updateRequest = new KnowledgeRequestDTO.Update();
        updateRequest.setTitle("수정된 지식 글");
        updateRequest.setContent("수정된 내용");
        updateRequest.setType(KnowledgeType.CAREERS);

        // When
        knowledge.update(updateRequest, null, null);

        // Then
        assertThat(knowledge.getImages()).containsExactly("https://s3.aws.com/old_image.png");
        assertThat(knowledge.getFiles()).containsExactly("https://s3.aws.com/old_file.pdf");
    }
}