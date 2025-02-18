package SMU.BAMBOO.Hompage.domain.award.service;

import SMU.BAMBOO.Hompage.domain.award.dto.AwardRequestDTO;
import SMU.BAMBOO.Hompage.domain.award.dto.AwardResponseDTO;
import SMU.BAMBOO.Hompage.domain.award.entity.Award;
import SMU.BAMBOO.Hompage.domain.award.repository.AwardRepository;
import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.inventory.repository.InventoryRepository;
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

    /**
     * 명예의 전당 (Award) 생성
     */
    @Override
    @Transactional
    public AwardResponseDTO.Create create(AwardRequestDTO.Create request) {
        Inventory inventory = inventoryRepository.getById(request.inventoryId());

        Award award = Award.builder()
                .inventory(inventory)
                .batch(request.batch())
                .title(request.title())
                .week(request.week())
                .startDate(request.startDate())
                .endDate(request.endDate())
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
        getById(awardId);
        awardRepository.delete(awardId);
    }
}
