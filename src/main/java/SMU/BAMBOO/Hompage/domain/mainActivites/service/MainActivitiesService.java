package SMU.BAMBOO.Hompage.domain.mainActivites.service;

import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MainActivitiesService {
    MainActivitiesResponseDTO.Detail create(MainActivitiesRequestDTO.Create dto, List<String> images, Member member);
    Page<MainActivitiesResponseDTO.ActivitiesByYearResponse> getMainActivitiesByYear(int year, int page, int size);
    MainActivitiesResponseDTO.Detail getMainActivity(Long id);
    void deleteMainActivity(Long id, Member member);
    void updateMainActivity(Long id, MainActivitiesRequestDTO.Update request, List<String> images, Member member);
}
