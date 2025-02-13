package SMU.BAMBOO.Hompage.domain.tag.controller;

import SMU.BAMBOO.Hompage.domain.tag.dto.TagRequestDTO;
import SMU.BAMBOO.Hompage.domain.tag.dto.TagResponseDTO;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.mock.container.TestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TagControllerTest {

    private TestContainer testContainer;

    @BeforeEach
    void setUp() {
        testContainer = new TestContainer();
    }

    @Test
    void 태그_생성_Controller_테스트() {
        // Given
        TagRequestDTO.Create request = new TagRequestDTO.Create("CV");

        // When
        SuccessResponse<TagResponseDTO.Create> response = testContainer.tagController.create(request);

        // Then
        assertThat(response.getResult().name()).isEqualTo("CV");
    }

    @Test
    void 태그_ID_조회_Controller_테스트() {
        // Given
        TagRequestDTO.Create request = new TagRequestDTO.Create("CV");
        TagResponseDTO.Create tag = testContainer.tagService.create(request);

        // When
        SuccessResponse<TagResponseDTO.GetOne> response = testContainer.tagController.getOne(tag.tagId());

        // Then
        assertThat(response.getResult().name()).isEqualTo("CV");
    }

    @Test
    void 태그_이름_조회_Controller_테스트() {
        // Given
        TagRequestDTO.Create request = new TagRequestDTO.Create("CV");
        testContainer.tagService.create(request);

        // When
        SuccessResponse<TagResponseDTO.GetOne> response = testContainer.tagController.getOneByName("CV");

        // Then
        assertThat(response.getResult().name()).isEqualTo("CV");
    }

    @Test
    void 태그_전체_조회_Controller_테스트() {
        // Given
        testContainer.tagService.create(new TagRequestDTO.Create("CV"));
        testContainer.tagService.create(new TagRequestDTO.Create("ML"));
        testContainer.tagService.create(new TagRequestDTO.Create("DL"));

        // When
        SuccessResponse<List<TagResponseDTO.GetOne>> response = testContainer.tagController.findAll();

        // Then
        assertThat(response.getResult()).hasSize(3);
        assertThat(response.getResult()).extracting(TagResponseDTO.GetOne::name)
                .containsExactlyInAnyOrder("CV", "ML", "DL");
    }

    @Test
    void 태그_삭제_Controller_테스트() {
        // Given
        TagRequestDTO.Create request = new TagRequestDTO.Create("DA");
        TagResponseDTO.Create tag = testContainer.tagService.create(request);

        // When
        testContainer.tagController.delete(tag.tagId());

        // Then
        assertThrows(CustomException.class, () -> testContainer.tagController.getOne(tag.tagId()));
    }
}