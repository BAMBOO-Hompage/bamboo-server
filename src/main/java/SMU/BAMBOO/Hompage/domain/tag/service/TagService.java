package SMU.BAMBOO.Hompage.domain.tag.service;

import SMU.BAMBOO.Hompage.domain.tag.dto.TagRequestDTO;
import SMU.BAMBOO.Hompage.domain.tag.dto.TagResponseDTO;

import java.util.List;

public interface TagService {

    TagResponseDTO.Create create(TagRequestDTO.Create dto);
    TagResponseDTO.GetOne getById(Long id);
    TagResponseDTO.GetOne getByName(String name);
    List<TagResponseDTO.GetOne> findAll();
    void delete(Long id);
}
