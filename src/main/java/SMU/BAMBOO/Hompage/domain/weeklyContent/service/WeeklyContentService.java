package SMU.BAMBOO.Hompage.domain.weeklyContent.service;

import SMU.BAMBOO.Hompage.domain.weeklyContent.dto.WeeklyContentRequestDTO;
import SMU.BAMBOO.Hompage.domain.weeklyContent.dto.WeeklyContentResponseDTO;

import java.util.List;

public interface WeeklyContentService {

    WeeklyContentResponseDTO.Create create(Long memberId, Long subjectId, WeeklyContentRequestDTO.Create request);
    WeeklyContentResponseDTO.GetOne getById(Long id);
    List<WeeklyContentResponseDTO.GetOne> getWeeklyContentBySubjectId(Long subjectId);
    WeeklyContentResponseDTO.Update update(Long id, Long subjectId, WeeklyContentRequestDTO.Update request);
    void delete(Long id, Long subjectId);
}
