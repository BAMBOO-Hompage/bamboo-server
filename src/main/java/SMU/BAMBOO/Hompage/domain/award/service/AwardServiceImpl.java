package SMU.BAMBOO.Hompage.domain.award.service;

import SMU.BAMBOO.Hompage.domain.award.dto.AwardRequestDTO;
import SMU.BAMBOO.Hompage.domain.award.dto.AwardResponseDTO;
import SMU.BAMBOO.Hompage.domain.award.entity.Award;
import SMU.BAMBOO.Hompage.domain.award.repository.AwardRepository;
import SMU.BAMBOO.Hompage.domain.cohort.repository.CohortRepository;
import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.inventory.repository.InventoryRepository;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
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
public class AwardServiceImpl implements AwardService {

    private final AwardRepository awardRepository;
    private final InventoryRepository inventoryRepository;
    private final CohortRepository cohortRepository;
    private final SubjectRepository subjectRepository;
    private final MemberRepository memberRepository;

    /**
     * 명예의 전당 (Award) 생성
     */
    @Override
    @Transactional
    public AwardResponseDTO.Create create(AwardRequestDTO.Create request) {
        validateDuplicateAward(request.inventoryId());

        Inventory inventory = inventoryRepository.getById(request.inventoryId());
        cohortRepository.getByBatch(request.batch());
        Subject subject = subjectRepository.getByBatchAndName(request.batch(), request.subjectName());
        memberRepository.getById(request.memberId());

        Award award = Award.builder()
                .inventory(inventory)
                .subject(subject)
                .batch(request.batch())
                .isMidterm(request.isMidterm())
                .build();

        Award savedAward = awardRepository.save(award);
        return AwardResponseDTO.Create.from(savedAward);
    }

    /**
     * ID로 명예의 전당 (Award) 단일 조회
     */
    @Override
    public AwardResponseDTO.GetOne getById(Long id) {
        Award award = awardRepository.getById(id);
        return AwardResponseDTO.GetOne.from(award);
    }

    /**
     * 명예의 전당 (Award) 전체 조회
     */
    @Override
    public List<AwardResponseDTO.GetOne> findAll() {
        List<Award> awards = awardRepository.findAll();
        return awards.stream()
                .map(AwardResponseDTO.GetOne::from)
                .toList();
    }

    /**
     * 기수를 기준으로 명예의 전당 (Award) 조회
     */
    @Override
    public List<AwardResponseDTO.GetOne> getAwardsByBatch(int batch) {
        List<Award> awards = awardRepository.findByBatch(batch);
        return awards.stream()
                .map(AwardResponseDTO.GetOne::from)
                .toList();
    }

    /**
     * 명예의 전당 (Award) 수정
     */
    @Override
    @Transactional
    public AwardResponseDTO.Update update(Long awardId, AwardRequestDTO.Update request) {
        Award award = awardRepository.getById(awardId);
        Inventory inventory = inventoryRepository.getById(request.inventoryId());
        award.updateAward(inventory, request);

        return AwardResponseDTO.Update.from(award);
    }

    /**
     * 명예의 전당 (Award) 삭제
     */
    @Override
    @Transactional
    public void delete(Long awardId) {
        Award award = awardRepository.getById(awardId);

        // Inventory와의 관계 해제
        award.setInventory(null);
        awardRepository.save(award);

        awardRepository.delete(awardId);
    }

    /**
     * 명예의 전당 중복 등록 방지
     */
    private void validateDuplicateAward(Long inventoryId) {
        boolean exists = awardRepository.existsByInventoryId(inventoryId);
        if (exists) {
            throw new CustomException(ErrorCode.DUPLICATE_AWARD);
        }
    }
}
