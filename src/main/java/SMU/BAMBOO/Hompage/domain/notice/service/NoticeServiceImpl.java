package SMU.BAMBOO.Hompage.domain.notice.service;

import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeRequestDTO;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeResponseDTO;
import SMU.BAMBOO.Hompage.domain.notice.entity.Notice;
import SMU.BAMBOO.Hompage.domain.notice.repository.NoticeRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public NoticeResponseDTO.Detail create(NoticeRequestDTO.Create request, Member member, List<MultipartFile> images, List<MultipartFile> files){

        if (!"ROLE_ADMIN".equals(member.getRole().name()) && !"ROLE_OPS".equals(member.getRole().name())) {
            throw new CustomException(ErrorCode.USER_NO_PERMISSION);
        }

        List<String> imageUrls = (images != null && !images.isEmpty())
                ? awsS3Service.uploadFiles("notice/images", images, true)
                : new ArrayList<>();
        List<String> fileUrls = (files != null && !files.isEmpty())
                ? awsS3Service.uploadFiles("notice/files", files, false)
                : new ArrayList<>();

        Notice notice = Notice.from(request, member, imageUrls, fileUrls);

        Notice savedNotice = noticeRepository.save(notice);

        return NoticeResponseDTO.Detail.from(savedNotice);
    }

}
