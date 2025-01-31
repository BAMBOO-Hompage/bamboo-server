package SMU.BAMBOO.Hompage.domain.inventory.service;

import SMU.BAMBOO.Hompage.domain.inventory.dto.InventoryRequestDTO;
import SMU.BAMBOO.Hompage.domain.inventory.dto.InventoryResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {

    InventoryResponseDTO.Create create(Long memberId, InventoryRequestDTO.Create request);
    InventoryResponseDTO.GetOne getById(Long id);
    Page<InventoryResponseDTO.GetOne> getInventories(int page, int size);
    List<InventoryResponseDTO.GetOne> findAll();
    InventoryResponseDTO.Update update(Long id, InventoryRequestDTO.Update request);
    // 어워드 추가 메서드 추가 예정
    void delete(Long id);
}
