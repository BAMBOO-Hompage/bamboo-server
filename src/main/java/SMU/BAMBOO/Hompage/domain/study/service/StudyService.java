package SMU.BAMBOO.Hompage.domain.study.service;

import SMU.BAMBOO.Hompage.domain.study.dto.StudyRequestDTO;
import SMU.BAMBOO.Hompage.domain.study.dto.StudyResponseDTO;

import java.util.List;

public interface StudyService {
    StudyResponseDTO.Create create(StudyRequestDTO.Create dto);
    StudyResponseDTO.GetOneWithAttendance getById(Long studyId);
    List<StudyResponseDTO.GetOne> findAll();
    List<StudyResponseDTO.GetOne> getStudiesByCohortAndSubject(int batchId, Long subjectId);
    StudyResponseDTO.Update update(Long studyId, StudyRequestDTO.Update dto);
    void delete(Long studyId);
}
