package SMU.BAMBOO.Hompage.domain.tag.entity;

import SMU.BAMBOO.Hompage.domain.tag.dto.TagRequestDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TagTest {

    @Test
    public void Tag_from_생성_테스트() {
        //given
        TagRequestDTO.Create request = new TagRequestDTO.Create("tag1");

        //when
        Tag tag = Tag.from(request);

        //then
        assertThat(tag).isNotNull();
        assertThat(tag.getName()).isEqualTo("tag1");
    }
}