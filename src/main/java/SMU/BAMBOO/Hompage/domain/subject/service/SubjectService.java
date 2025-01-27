package SMU.BAMBOO.Hompage.domain.subject.service;

import SMU.BAMBOO.Hompage.domain.subject.dto.SubjectRequestDTO;
import SMU.BAMBOO.Hompage.domain.subject.dto.SubjectResponseDTO;

import java.util.List;

public interface SubjectService {

    SubjectResponseDTO.Create create(SubjectRequestDTO.Create dto);
    SubjectResponseDTO.GetOne getById(Long id);
    List<SubjectResponseDTO.GetOne> findAll();
    SubjectResponseDTO.Update update(Long id, SubjectRequestDTO.Update dto);
    void delete(Long id);
}
