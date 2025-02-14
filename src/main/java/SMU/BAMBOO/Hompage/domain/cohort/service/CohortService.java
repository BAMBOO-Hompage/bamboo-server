package SMU.BAMBOO.Hompage.domain.cohort.service;

import SMU.BAMBOO.Hompage.domain.cohort.dto.CohortRequestDTO;
import SMU.BAMBOO.Hompage.domain.cohort.dto.CohortResponseDTO;

import java.util.List;

public interface CohortService {
    CohortResponseDTO.Create create(CohortRequestDTO.Create dto);
    CohortResponseDTO.GetOne getById(Long id);
    CohortResponseDTO.GetOne getByYearAndSemester(int year, boolean isFirstSemester);
    List<CohortResponseDTO.GetOne> getByBatch(int batch);
    List<CohortResponseDTO.GetOne> findAll();
    void delete(Long id);
}
