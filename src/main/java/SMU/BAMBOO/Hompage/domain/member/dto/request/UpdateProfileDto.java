package SMU.BAMBOO.Hompage.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Schema(description = "프로필 수정 요청 DTO")
public class UpdateProfileDto {

    @Schema(description = "수정할 전화번호", example = "01088889999")
    private String phoneNumber;

    @Schema(description = "수정할 이미지")
    private MultipartFile profileImage;
}
