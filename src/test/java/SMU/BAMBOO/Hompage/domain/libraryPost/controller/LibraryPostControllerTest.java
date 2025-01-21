package SMU.BAMBOO.Hompage.domain.libraryPost.controller;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostRequestDTO;
import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
import SMU.BAMBOO.Hompage.mock.TestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LibraryPostControllerTest {

    private TestContainer testContainer;

    @BeforeEach
    void setUp() {
        testContainer = new TestContainer();
    }

    @Test
    void LibraryPost_생성_Controller_테스트() {
        // Given
        testContainer.tagRepository.save(Tag.builder().tagId(1L).name("CV").build());
        testContainer.tagRepository.save(Tag.builder().tagId(2L).name("ML").build());

        LibraryPostRequestDTO.Create request = new LibraryPostRequestDTO.Create(
                "http://link.com",
                2025,
                "김재관",
                "CV 논문",
                "CV의 ~에 대하여",
                List.of("CV", "ML")
        );

        Member testMember = Member.builder()
                .memberId(1L)
                .email("test1@naver.com")
                .pw("1234")
                .name("kim")
                .major("휴먼")
                .phone("010-1111-1111")
                .role(Role.ROLE_USER)
                .build();

        // When
        LibraryPostResponseDTO.Create response =
                testContainer.libraryPostService.create(request, testMember);

        // Then
        assertThat(response.link()).isEqualTo("http://link.com");
        assertThat(response.year()).isEqualTo(2025);
        assertThat(response.speaker()).isEqualTo("김재관");
        assertThat(response.paperName()).isEqualTo("CV 논문");
        assertThat(response.topic()).isEqualTo("CV의 ~에 대하여");
        assertThat(response.tagNames()).isEqualTo(List.of("CV", "ML"));
    }
}
