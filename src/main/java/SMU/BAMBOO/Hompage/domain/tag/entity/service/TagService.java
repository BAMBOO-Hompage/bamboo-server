package SMU.BAMBOO.Hompage.domain.tag.entity.service;

import SMU.BAMBOO.Hompage.domain.tag.entity.dto.TagRequestDTO;
import SMU.BAMBOO.Hompage.domain.tag.entity.dto.TagResponseDTO;

import java.util.List;

public interface TagService {

    TagResponseDTO.Create create(TagRequestDTO.Create dto);
    TagResponseDTO.GetOne getById(Long id);
    TagResponseDTO.GetOne getByName(String name);
    List<TagResponseDTO.GetOne> findAll();
    void delete(Long id);
}
