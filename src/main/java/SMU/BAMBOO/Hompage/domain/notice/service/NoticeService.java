package SMU.BAMBOO.Hompage.domain.notice.service;


import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeRequestDTO;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoticeService {
    NoticeResponseDTO.Detail create(NoticeRequestDTO.Create request, Member member, List<MultipartFile> images, List<MultipartFile> files);

}
