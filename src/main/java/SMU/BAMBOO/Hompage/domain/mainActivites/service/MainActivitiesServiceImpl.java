package SMU.BAMBOO.Hompage.domain.mainActivites.service;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesResponseDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.entity.MainActivities;
import SMU.BAMBOO.Hompage.domain.mainActivites.repository.MainActivitiesRepository;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainActivitiesServiceImpl implements MainActivitiesService{

    private final MainActivitiesRepository mainActivitiesRepository;

    @Override
    public MainActivitiesResponseDTO.Create create(MainActivitiesRequestDTO.Create request, List<String> images) {

        // Hardcoded member
        Member hardcodedMember = Member.builder()
                .memberId(1L)
                .email("test@example.com")
                .pw("password")
                .name("John Doe")
                .studentId("202312345")
                .major("Computer Science")
                .phone("01012345678")
                .role(Role.USER) // Adjust based on your Role enum
                .build();

        MainActivities mainActivities = MainActivities.from(request, hardcodedMember, images);

        MainActivities savedMainActivities = mainActivitiesRepository.save(mainActivities);

        return MainActivitiesResponseDTO.Create.from(savedMainActivities);
    }
}
