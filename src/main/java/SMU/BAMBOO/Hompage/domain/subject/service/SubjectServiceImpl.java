package SMU.BAMBOO.Hompage.domain.subject.service;

import SMU.BAMBOO.Hompage.domain.study.dto.StudyResponseDTO;
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
import java.util.stream.Collectors;

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
        if (subjectRepository.findByName(dto.name()).isPresent()) {
            throw new CustomException(ErrorCode.SUBJECT_ALREADY_EXIST);
        }

        Subject subject = Subject.builder()
                .name(dto.name())
                .isBook(dto.isBook())
                .build();

        subjectRepository.save(subject);
        return SubjectResponseDTO.Create.from(subject);
    }

    @Override
    public SubjectResponseDTO.GetOne getById(Long id) {
        Subject subject = getSubjectById(id);
        return SubjectResponseDTO.GetOne.from(subject);
    }

    public List<SubjectResponseDTO.GetOne> findAll(Boolean isBook) {
        if (isBook == null) {
            return subjectRepository.findAll().stream()
                    .map(SubjectResponseDTO.GetOne::from)
                    .toList();
        }
        return subjectRepository.findByIsBook(isBook).stream()
                .map(SubjectResponseDTO.GetOne::from)
                .toList();
    }

    @Override
    public List<StudyResponseDTO.GetOne> getStudiesBySubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_EXIST));

        return subject.getStudies().stream()
                .map(StudyResponseDTO.GetOne::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SubjectResponseDTO.Update update(Long id, SubjectRequestDTO.Update dto) {
        Subject subject = getSubjectById(id);
        subject.update(dto);
        return SubjectResponseDTO.Update.from(subject);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getSubjectById(id);
        subjectRepository.deleteById(id);
    }
}
