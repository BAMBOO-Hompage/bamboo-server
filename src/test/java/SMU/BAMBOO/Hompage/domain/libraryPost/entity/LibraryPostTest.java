package SMU.BAMBOO.Hompage.domain.libraryPost.entity;

import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostRequestDTO;
import SMU.BAMBOO.Hompage.domain.mapping.LibraryPostTag;
import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LibraryPostTest {

    @Test
    void UpdateBasicFields_테스트() {
        // Given
        LibraryPost libraryPost = LibraryPost.builder()
                .libraryPostId(1L)
                .paperName("CV 논문1")
                .year("2025")
                .topic("CV의 ~에 대하여")
                .content("최신 논문에 대한 정리")
                .link("http://link.com")
                .build();

        LibraryPostRequestDTO.Update request = new LibraryPostRequestDTO.Update(
                "http://linkkk.com",
                "2024",
                "CV 논문2",
                "머신러닝의 ~에 대하여",
                "[수정] 최신 논문에 대한 정리",
                List.of()
        );

        // When
        libraryPost.updateBasicFields(request);

        // Then
        assertThat(libraryPost.getPaperName()).isEqualTo("CV 논문2");
        assertThat(libraryPost.getYear()).isEqualTo("2024");
        assertThat(libraryPost.getTopic()).isEqualTo("머신러닝의 ~에 대하여");
        assertThat(libraryPost.getContent()).isEqualTo("[수정] 최신 논문에 대한 정리");
        assertThat(libraryPost.getLink()).isEqualTo("http://linkkk.com");
    }

    @Test
    void LibraryPostTag_연관관계_설정_테스트() {
        // Given
        LibraryPost libraryPost = LibraryPost.builder().build();
        Tag tag = Tag.builder()
                .tagId(1L)
                .name("AI")
                .build();
        LibraryPostTag libraryPostTag = LibraryPostTag.builder()
                .tag(tag)
                .libraryPost(libraryPost)
                .build();

        // When
        libraryPost.addLibraryPostTag(libraryPostTag);

        // Then
        assertThat(libraryPost.getLibraryPostTags()).hasSize(1);
        assertThat(libraryPost.getLibraryPostTags().get(0).getTag().getName()).isEqualTo("AI");
        assertThat(libraryPostTag.getLibraryPost()).isEqualTo(libraryPost);
    }

    @Test
    void testSetTags() {
        // Given
        LibraryPost libraryPost = LibraryPost.builder().build();
        List<Tag> initialTags = List.of(Tag.builder().tagId(1L).name("CV").build());
        libraryPost.addTags(initialTags);

        List<Tag> newTags = List.of(
                Tag.builder().tagId(2L).name("AI").build(),
                Tag.builder().tagId(3L).name("ML").build()
        );

        // When
        libraryPost.setTags(newTags);

        // Then
        assertThat(libraryPost.getLibraryPostTags()).hasSize(2);
        assertThat(libraryPost.getLibraryPostTags())
                .extracting(libraryPostTag -> libraryPostTag.getTag().getName())
                .contains("AI", "ML");
    }

}