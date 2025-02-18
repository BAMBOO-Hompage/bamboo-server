package SMU.BAMBOO.Hompage.domain.subject.service;

import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;
import SMU.BAMBOO.Hompage.domain.cohort.repository.CohortRepository;
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
    private final CohortRepository cohortRepository;

    private Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_EXIST));
    }

    /**
     * 과목 생성 - 이름으로 중복 검증
     */
    @Override
    @Transactional
    public SubjectResponseDTO.Create create(SubjectRequestDTO.Create dto) {
        Cohort cohort = cohortRepository.getByBatch(dto.batch());

        if (subjectRepository.findByName(dto.name()).isPresent()) {
            throw new CustomException(ErrorCode.SUBJECT_ALREADY_EXIST);
        }

        Subject subject = Subject.builder()
                .name(dto.name())
                .bookName(dto.bookName())
                .isBook(dto.isBook())
                .cohort(cohort)
                .build();

        subjectRepository.save(subject);
        return SubjectResponseDTO.Create.from(subject);
    }

    /**
     * 과목 ID로 단일 조회
     */
    @Override
    public SubjectResponseDTO.GetOne getById(Long id) {
        Subject subject = getSubjectById(id);
        return SubjectResponseDTO.GetOne.from(subject);
    }

    /**
     * 과목 목록 조회
     * @ isBook이 null 이면 전체 조회
     * @ true -> 커리큘럼 과목 조회
     * @ false -> 자율 과목 조회
     */
    @Override
    public List<SubjectResponseDTO.GetOne> findAll(Boolean isBook, int batch) {
        return subjectRepository.findByIsBookAndBatch(isBook, batch)
                .stream()
                .map(SubjectResponseDTO.GetOne::from)
                .toList();
    }

    /**
     * 과목별 스터디 조회
     */
    @Override
    public List<StudyResponseDTO.GetOne> getStudiesBySubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_EXIST));

        return subject.getStudies().stream()
                .map(StudyResponseDTO.GetOne::from)
                .collect(Collectors.toList());
    }

    /**
     * 과목 정보 수정
     */
    @Override
    @Transactional
    public SubjectResponseDTO.Update update(Long id, SubjectRequestDTO.Update dto) {
        Cohort cohort = cohortRepository.getByBatch(dto.batch());
        Subject subject = getSubjectById(id);
        subject.update(dto, cohort);
        return SubjectResponseDTO.Update.from(subject);
    }

    /**
     * 과목 삭제
     */
    @Override
    @Transactional
    public void delete(Long id) {
        getSubjectById(id);
        subjectRepository.deleteById(id);
    }
}
