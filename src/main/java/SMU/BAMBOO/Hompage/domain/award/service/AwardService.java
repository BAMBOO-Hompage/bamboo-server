package SMU.BAMBOO.Hompage.domain.award.service;

import SMU.BAMBOO.Hompage.domain.award.dto.AwardRequestDTO;
import SMU.BAMBOO.Hompage.domain.award.dto.AwardResponseDTO;

import java.util.List;

public interface AwardService {
    AwardResponseDTO.Create create(AwardRequestDTO.Create dto);
    AwardResponseDTO.GetOne getById(Long id);
    List<AwardResponseDTO.GetOne> findAll();
    List<AwardResponseDTO.GetOne> getAwardsByBatch(int batch);
    AwardResponseDTO.Update update(Long awardId, AwardRequestDTO.Update dto);
    void delete(Long awardId);
    List<AwardResponseDTO.GetOne> getLatestWeekAwardsByBatch(int batch);
}
