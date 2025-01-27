package SMU.BAMBOO.Hompage.domain.subject.service;

import SMU.BAMBOO.Hompage.domain.enums.SubjectName;
import SMU.BAMBOO.Hompage.domain.subject.dto.SubjectRequestDTO;
import SMU.BAMBOO.Hompage.domain.subject.dto.SubjectResponseDTO;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.domain.subject.repository.SubjectRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    private Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_EXIST));
    }

    @Override
    @Transactional
    public SubjectResponseDTO.Create create(SubjectRequestDTO.Create dto) {
        try {
            Subject subject = Subject.builder()
                    .name(SubjectName.valueOf(dto.name()))
                    .build();

            subjectRepository.save(subject);
            return SubjectResponseDTO.Create.from(subject);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.SUBJECT_INVALID);
        }
    }

    @Override
    public SubjectResponseDTO.GetOne getById(Long id) {
        Subject subject = getSubjectById(id);
        return SubjectResponseDTO.GetOne.from(subject);
    }

    @Override
    public List<SubjectResponseDTO.GetOne> findAll() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(SubjectResponseDTO.GetOne::from)
                .toList();
    }

    @Override
    @Transactional
    public SubjectResponseDTO.Update update(Long id, SubjectRequestDTO.Update dto) {
        Subject subject = getSubjectById(id);
        subject.updateName(dto);
        return SubjectResponseDTO.Update.from(subject);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getSubjectById(id);
        subjectRepository.deleteById(id);
    }
}
