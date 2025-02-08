package SMU.BAMBOO.Hompage.domain.mainActivites.entity;

import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MainActivitiesTest {

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
    void from_메서드_테스트() {
        // Given
        MainActivitiesRequestDTO.Create request = new MainActivitiesRequestDTO.Create();
        request.setTitle("BAMBOO 프로젝트 개발");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 2, 28));
        request.setYear(2025);

        List<String> imageUrls = List.of("https://s3.aws.com/image1.png", "https://s3.aws.com/image2.png");

        // When
        MainActivities mainActivity = MainActivities.from(request, testMember, imageUrls);

        // Then
        assertThat(mainActivity).isNotNull();
        assertThat(mainActivity.getMember()).isEqualTo(testMember);
        assertThat(mainActivity.getTitle()).isEqualTo("BAMBOO 프로젝트 개발");
        assertThat(mainActivity.getStartDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(mainActivity.getEndDate()).isEqualTo(LocalDate.of(2025, 2, 28));
        assertThat(mainActivity.getYear()).isEqualTo(2025);
        assertThat(mainActivity.getImages()).containsExactly("https://s3.aws.com/image1.png", "https://s3.aws.com/image2.png");
    }

    @Test
    void update_메서드_테스트() {
        // Given
        MainActivities mainActivity = MainActivities.builder()
                .member(testMember)
                .title("BAMBOO 프로젝트 개발")
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 2, 28))
                .year(2025)
                .images(List.of("https://s3.aws.com/old_image.png"))
                .build();

        MainActivitiesRequestDTO.Update updateRequest = new MainActivitiesRequestDTO.Update();
        updateRequest.setTitle("[수정] BAMBOO 프로젝트 개발");
        updateRequest.setStartDate(LocalDate.of(2025, 1, 10));
        updateRequest.setEndDate(LocalDate.of(2025, 3, 4));
        updateRequest.setYear(2025);

        List<String> updatedImages = List.of("https://s3.aws.com/new_image.png");

        // When
        mainActivity.update(updateRequest, updatedImages);

        // Then
        assertThat(mainActivity.getTitle()).isEqualTo("[수정] BAMBOO 프로젝트 개발");
        assertThat(mainActivity.getStartDate()).isEqualTo(LocalDate.of(2025, 1, 10));
        assertThat(mainActivity.getEndDate()).isEqualTo(LocalDate.of(2025, 3, 4));
        assertThat(mainActivity.getYear()).isEqualTo(2025);
        assertThat(mainActivity.getImages()).containsExactly("https://s3.aws.com/new_image.png");
    }

}