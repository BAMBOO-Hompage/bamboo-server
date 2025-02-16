package SMU.BAMBOO.Hompage.domain.cohort.service;

import SMU.BAMBOO.Hompage.domain.cohort.dto.CohortRequestDTO;
import SMU.BAMBOO.Hompage.domain.cohort.dto.CohortResponseDTO;
import SMU.BAMBOO.Hompage.domain.cohort.entity.Cohort;
import SMU.BAMBOO.Hompage.domain.cohort.repository.CohortRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CohortServiceImpl implements CohortService {

    private final CohortRepository cohortRepository;

    /** ID로 기수 조회 */
    private Cohort getCohortById(Long id) {
        return cohortRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COHORT_NOT_EXIST));
    }

    /**
     * 가장 최신 기수를 조회
     */
    @Override
    public CohortResponseDTO.GetOne getLatestCohort() {
        Cohort latestCohort = cohortRepository.findTopByOrderByBatchDesc()
                .orElseThrow(() -> new CustomException(ErrorCode.COHORT_NOT_FOUND_CURRENT));
        return CohortResponseDTO.GetOne.from(latestCohort);
    }

    /**
     * 기수 정보 생성
     */
    @Override
    @Transactional
    public CohortResponseDTO.Create create(CohortRequestDTO.Create dto) {
        if (cohortRepository.findByBatch(dto.batch()).isPresent()) {
            throw new CustomException(ErrorCode.COHORT_ALREADY_EXIST);
        }

        Cohort cohort = Cohort.builder()
                .batch(dto.batch())
                .year(dto.year())
                .isFirstSemester(dto.isFirstSemester())
                .build();

        Cohort savedCohort = cohortRepository.save(cohort);
        return CohortResponseDTO.Create.from(savedCohort);
    }

    /**
     * 기수 정보 ID로 조회
     */
    @Override
    public CohortResponseDTO.GetOne getById(Long id) {
        Cohort cohort = getCohortById(id);
        return CohortResponseDTO.GetOne.from(cohort);
    }

    /**
     * 기수로 해당 기수 정보 조회
     */
    @Override
    public CohortResponseDTO.GetOne getByBatch(int batch) {
        Cohort cohort = cohortRepository.findByBatch(batch)
                .orElseThrow(() -> new CustomException(ErrorCode.COHORT_NOT_EXIST));
        return CohortResponseDTO.GetOne.from(cohort);
    }

    /**
     * 기수 정보 전체 조회
     */
    @Override
    public List<CohortResponseDTO.GetOne> findAll() {
        List<Cohort> cohorts = cohortRepository.findAll();
        return cohorts.stream()
                .map(CohortResponseDTO.GetOne::from)
                .toList();
    }

    /**
     * 기수 정보 삭제
     */
    @Override
    @Transactional
    public void delete(Long id) {
        getCohortById(id);
        cohortRepository.deleteById(id);
    }
}
