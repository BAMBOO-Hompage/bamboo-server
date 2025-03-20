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
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final AwsS3Service awsS3Service;

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
    public InventoryResponseDTO.Create create(Long memberId, InventoryRequestDTO.Create request, MultipartFile file) {

        // 객체 조회 - 스터디, 회원
        Study study = studyRepository.findById(request.studyId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_EXIST));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        // 동일한 사람이 같은 주차에 이미 작성했는지 확인
        if (inventoryRepository.existsByMemberAndStudyAndWeek(member, study, request.week())) {
            throw new CustomException(ErrorCode.INVENTORY_ALREADY_EXIST);
        }

        String fileUrl = null;
        if (file != null && !file.isEmpty()) {
            try {
                fileUrl = awsS3Service.uploadFile("inventory/pdf", file, false);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.UPLOAD_FAILED);
            }
        }

        // 스터디 정리본 객체 생성
        Inventory inventory = Inventory.builder()
                .study(study)
                .member(member)
                .title(request.title())
                .content(request.content())
                .week(request.week())
                .isWeeklyBest(false)
                .fileUrl(fileUrl)
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
    public InventoryResponseDTO.Update update(Long id, InventoryRequestDTO.Update request, MultipartFile file) {
        // 객체 조회 및 수정
        Inventory inventory = getInventoryById(id);

        String fileUrl = null;
        if (file != null && !file.isEmpty()) {
            // 기존 파일 삭제
            if (inventory.getFileUrl() != null) {
                awsS3Service.deleteFile(awsS3Service.extractS3Key(inventory.getFileUrl()));
            }

            // 새로운 파일 업로드
            try {
                fileUrl = awsS3Service.uploadFile("inventory/pdf", file, false);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.UPLOAD_FAILED);
            }
        }

        inventory.updateInventory(request, fileUrl);

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

    @Override
    public InventoryResponseDTO.GetOne getInventoriesByMemberAndWeek(Long memberId, int week) {
        memberRepository.getById(memberId);
        Inventory inventory = inventoryRepository.findByMemberIdAndWeek(memberId, week)
                .orElseThrow(() -> new CustomException(ErrorCode.INVENTORY_NOT_EXIST));
        return InventoryResponseDTO.GetOne.from(inventory);
    }

    /**
     * weekly-best 선정
     */
    @Override
    @Transactional
    public void setWeeklyBest(Long studyId, int week, Long memberId) {
        Inventory inventory = inventoryRepository.findByStudyIdAndWeekAndMemberId(studyId, week, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVENTORY_NOT_EXIST));

        // 기존 주간 베스트 해제
        inventoryRepository.resetWeeklyBest(studyId, week);

        // 해당 정리본을 주간 베스트로 설정
        inventory.markAsWeeklyBest();
        inventoryRepository.save(inventory);
    }

    /**
     * 특정 스터디의 특정 주차의 weekly best 정리본 조회
     */
    @Override
    public InventoryResponseDTO.GetOne getWeeklyBestInventory(Long studyId, int week) {
        return inventoryRepository.findWeeklyBestByStudyIdAndWeek(studyId, week)
                .map(InventoryResponseDTO.GetOne::from)
                .orElse(null);
    }
}
