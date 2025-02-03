package SMU.BAMBOO.Hompage.domain.tag.service;

import SMU.BAMBOO.Hompage.domain.tag.dto.TagRequestDTO;
import SMU.BAMBOO.Hompage.domain.tag.dto.TagResponseDTO;
import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.mock.FakeTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TagServiceImplTest {

    private FakeTagRepository fakeTagRepository;
    private TagServiceImpl tagService;
    private Tag testTag1;
    private Tag testTag2;

    @BeforeEach
    void setUp() {
        fakeTagRepository = new FakeTagRepository();
        tagService = TagServiceImpl.builder()
                .tagRepository(fakeTagRepository)
                .build();

        testTag1 = Tag.builder()
                .name("CV")
                .build();

        testTag2 = Tag.builder()
                .name("DA")
                .build();
    }

    @Test
    void Tag_생성_테스트() {
        // Given
        TagRequestDTO.Create request = new TagRequestDTO.Create("CV");

        // When
        TagResponseDTO.Create response = tagService.create(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("CV");
    }

    @Test
    void 동일한_이름으로_태그_생성_불가() {
        // Given
        fakeTagRepository.save(testTag1);
        TagRequestDTO.Create request = new TagRequestDTO.Create("CV");

        // When
        // Then
        assertThatThrownBy(() -> tagService.create(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.TAG_ALREADY_EXIST.getMessage());
    }

    @Test
    void ID로_태그_조회_성공() {
        // Given
        fakeTagRepository.save(testTag1);

        // When
        TagResponseDTO.GetOne response = tagService.getById(1L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("CV");
    }

    @Test
    void ID로_존재하지_않는_태그_조회시_실패() {
        // Given
        fakeTagRepository.save(testTag1);

        // When
        // Then
        assertThatThrownBy(() -> tagService.getById(99L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.TAG_NOT_EXIST.getMessage());
    }

    @Test
    void 이름으로_태그_조회_성공() {
        // Given
        fakeTagRepository.save(testTag1);

        // When
        TagResponseDTO.GetOne response = tagService.getByName("CV");

        // Then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("CV");
    }

    @Test
    void Name으로_존재하지_않는_태그_조회시_실패() {
        // Given
        fakeTagRepository.save(testTag1);

        // When
        // Then
        assertThatThrownBy(() -> tagService.getByName("ML"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.TAG_NOT_EXIST.getMessage());
    }

    @Test
    void 전체_태그_조회() {
        // Given
        fakeTagRepository.save(testTag1);
        fakeTagRepository.save(testTag2);

        // When
        List<TagResponseDTO.GetOne> responses = tagService.findAll();

        // Then
        assertThat(responses).hasSize(2);
        assertThat(responses)
                .extracting(TagResponseDTO.GetOne::name)
                .contains("CV", "DA");
    }

    @Test
    void Tag_삭제_성공() {
        // Given
        Tag tag = fakeTagRepository.save(testTag1);

        // When
        tagService.delete(tag.getTagId());

        // Then
        assertThatThrownBy(() -> tagService.getById(tag.getTagId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.TAG_NOT_EXIST.getMessage());
    }

    @Test
    void 존재하지_않는_태그는_삭제_실패() {
        // Given
        fakeTagRepository.save(testTag1);

        // When
        // Then
        assertThatThrownBy(() -> tagService.delete(99L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.TAG_NOT_EXIST.getMessage());
    }
}