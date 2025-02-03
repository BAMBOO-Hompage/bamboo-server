package SMU.BAMBOO.Hompage.domain.tag.controller;

import SMU.BAMBOO.Hompage.domain.tag.dto.TagRequestDTO;
import SMU.BAMBOO.Hompage.domain.tag.dto.TagResponseDTO;
import SMU.BAMBOO.Hompage.domain.tag.service.TagService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Builder
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(name = "태그(알렉산드리아) API")
public class TagController {

    private final TagService tagService;

    @PostMapping
    @Operation(summary = "태그 등록")
    public SuccessResponse<TagResponseDTO.Create> create(@Valid @RequestBody TagRequestDTO.Create request) {
        TagResponseDTO.Create result = tagService.create(request);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/{tagId}")
    @Operation(summary = "ID로 태그 조회")
    public SuccessResponse<TagResponseDTO.GetOne> getOne(@PathVariable("tagId") Long id) {
        TagResponseDTO.GetOne result = tagService.getById(id);
        return SuccessResponse.ok(result);
    }

    @GetMapping()
    @Operation(summary = "Name 으로 태그 조회")
    public SuccessResponse<TagResponseDTO.GetOne> getOneByName(
            @RequestParam("name") String name
    ) {
        TagResponseDTO.GetOne result = tagService.getByName(name);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/all")
    @Operation(summary = "태그 전체 조회")
    public SuccessResponse<List<TagResponseDTO.GetOne>> findAll() {
        List<TagResponseDTO.GetOne> result = tagService.findAll();
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/{tagId}")
    @Operation(summary = "태그 삭제")
    public SuccessResponse<String> delete(
            @PathVariable("tagId") Long id
    ) {
        tagService.delete(id);
        return SuccessResponse.ok("태그 삭제에 성공했습니다.");
    }
}
