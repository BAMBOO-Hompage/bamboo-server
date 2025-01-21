package SMU.BAMBOO.Hompage.domain.tag.entity.service;

import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
import SMU.BAMBOO.Hompage.domain.tag.entity.dto.TagRequestDTO;
import SMU.BAMBOO.Hompage.domain.tag.entity.dto.TagResponseDTO;
import SMU.BAMBOO.Hompage.domain.tag.entity.repository.TagRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    /** ID로 태그 조회 */
    private Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_EXIST));
    }

    /** 이름으로 태그 조회 */
    private Tag getTagByName(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_EXIST));
    }

    @Override
    @Transactional
    public TagResponseDTO.Create create(TagRequestDTO.Create dto) {
        if (tagRepository.findByName(dto.name()).isPresent()) {
            throw new CustomException(ErrorCode.TAG_ALREADY_EXIST);
        }

        Tag tag = Tag.from(dto);
        Tag saveTag = tagRepository.save(tag);

        return TagResponseDTO.Create.from(saveTag);
    }

    @Override
    public TagResponseDTO.GetOne getById(Long id) {
        Tag tag = getTagById(id);
        return TagResponseDTO.GetOne.from(tag);
    }

    @Override
    public TagResponseDTO.GetOne getByName(String name) {
        Tag tag = getTagByName(name);
        return TagResponseDTO.GetOne.from(tag);
    }

    @Override
    public List<TagResponseDTO.GetOne> findAll() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(TagResponseDTO.GetOne::from)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getTagById(id);
        tagRepository.deleteById(id);
    }
}
