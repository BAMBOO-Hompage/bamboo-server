package SMU.BAMBOO.Hompage.domain.notice.service;

import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeRequestDTO;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoticeService {
    NoticeResponseDTO.Detail create(NoticeRequestDTO.Create request, Member member, List<MultipartFile> images, List<MultipartFile> files);

    NoticeResponseDTO.Detail getNotice(Long id);

    void deleteNotice(Long id, Member member);

    NoticeResponseDTO.Detail update(Long id, NoticeRequestDTO.Update request,
                                    List<String> imageUrls, List<MultipartFile> newImages,
                                    List<String> fileUrls, List<MultipartFile> newFiles);

    Page<NoticeResponseDTO.Detail> getNotices(PageRequest pageRequest);

    Page<NoticeResponseDTO.Detail> getNoticesByType(String type, PageRequest pageRequest);
}
