package SMU.BAMBOO.Hompage.domain.mainActivites.service;

import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MainActivitiesService {
    MainActivitiesResponseDTO.Create create(MainActivitiesRequestDTO.Create dto, List<String> images);
}
