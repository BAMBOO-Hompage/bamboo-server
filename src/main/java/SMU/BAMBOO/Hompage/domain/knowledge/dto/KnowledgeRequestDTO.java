package SMU.BAMBOO.Hompage.domain.knowledge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema(description = "지식공유 게시판 요청 DTO")
public class KnowledgeRequestDTO {

    @Schema(description = "지식공유 게시판 생성 요청 DTO")
    public record Create(
            @Schema(description = "제목", example = "Python 지식 공유") String title,
            @Schema(description = "내용", example = "~Python 지식 방출 중~") String content,
            @Schema(description = "타입", example = "RESOURCES") String type
    ) {}

    @Schema(description = "지식공유 게시판 수정 요청 DTO")
    public record Update(
            @Schema(description = "제목", example = "수정된 Python 지식 공유") String title,
            @Schema(description = "내용", example = "~수정된 Python 지식 방출 중~") String content,
            @Schema(description = "타입", example = "RESOURCES") String type
    ) {}
}
