package SMU.BAMBOO.Hompage.domain.inventory.service;

import SMU.BAMBOO.Hompage.domain.inventory.dto.InventoryRequestDTO;
import SMU.BAMBOO.Hompage.domain.inventory.dto.InventoryResponseDTO;
import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.inventory.repository.InventoryRepository;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.study.repository.StudyRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    /** ID로 스터디 정리본 조회 */
    private Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVENTORY_NOT_EXIST));
    }

    /**
     * 스터디 정리본 생성
     */
    @Override
    @Transactional
    public InventoryResponseDTO.Create create(Long memberId, InventoryRequestDTO.Create request) {

        // 객체 조회 - 스터디, 회원
        Study study = studyRepository.findById(request.studyId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_EXIST));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        // 동일한 사람이 같은 주차에 이미 작성했는지 확인
        if (inventoryRepository.existsByMemberAndStudyAndWeek(member, study, request.week())) {
            throw new CustomException(ErrorCode.INVENTORY_ALREADY_EXIST);
        }

        // 스터디 정리본 객체 생성
        Inventory inventory = Inventory.builder()
                .study(study)
                .member(member)
                .title(request.title())
                .content(request.content())
                .week(request.week())
                .build();

        // 저장
        Inventory savedInventory = inventoryRepository.save(inventory);
        return InventoryResponseDTO.Create.from(savedInventory);
    }

    /**
     * ID로 스터디 정리본 단건 조회
     */
    @Override
    public InventoryResponseDTO.GetOne getById(Long id) {
        Inventory inventory = getInventoryById(id);
        return InventoryResponseDTO.GetOne.from(inventory);
    }

    /**
     * 특정 스터디의 스터디 정리본 페이지 조회
     */
    public Page<InventoryResponseDTO.GetOne> getInventoriesByStudy(Long studyId, int page, int size) {
        studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_EXIST));

        Pageable pageable = PageRequest.of(page, size);
        Page<Inventory> inventories = inventoryRepository.findByStudy(studyId, pageable);

        return inventories.map(InventoryResponseDTO.GetOne::from);
    }

    /**
     * 스터디 정리본 페이지 조회
     */
    @Override
    public Page<InventoryResponseDTO.GetOne> getInventories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return inventoryRepository.findByPage(pageable)
                .map(InventoryResponseDTO.GetOne::from);
    }

    /**
     * 스터디 정리본 전체 조회
     */
    @Override
    public List<InventoryResponseDTO.GetOne> findAll() {
        List<Inventory> inventories = inventoryRepository.findAll();

        return inventories.stream()
                .map(InventoryResponseDTO.GetOne::from)
                .toList();
    }

    /**
     * 스터디 정리본 수정
     */
    @Override
    @Transactional
    public InventoryResponseDTO.Update update(Long id, InventoryRequestDTO.Update request) {
        // 객체 조회 및 수정
        Inventory inventory = getInventoryById(id);
        inventory.updateInventory(request);

        return InventoryResponseDTO.Update.from(inventory);
    }

    /**
     * 스터디 정리본 삭제
     */
    @Override
    @Transactional
    public void delete(Long id) {
        getInventoryById(id);
        inventoryRepository.deleteById(id);
    }
}
