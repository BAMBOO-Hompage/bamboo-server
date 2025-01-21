package SMU.BAMBOO.Hompage.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfileImageRequest {

    @NotNull(message = "이미지를 첨부해주세요.")
    private MultipartFile profileImage;
}
