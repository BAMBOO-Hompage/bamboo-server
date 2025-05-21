package SMU.BAMBOO.Hompage.domain.notice.service;

import SMU.BAMBOO.Hompage.domain.enums.NoticeType;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeRequestDTO;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeResponseDTO;
import SMU.BAMBOO.Hompage.domain.notice.entity.Notice;
import SMU.BAMBOO.Hompage.domain.notice.repository.NoticeRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Facade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{


    private final NoticeRepository noticeRepository;
    private final AwsS3Facade awsS3Facade;


    /** 생성 */
    @Override
    public NoticeResponseDTO.Detail create(NoticeRequestDTO.Create request, Member member, List<MultipartFile> images, List<MultipartFile> files){

        if (!"ROLE_ADMIN".equals(member.getRole().name()) && !"ROLE_OPS".equals(member.getRole().name())) {
            throw new CustomException(ErrorCode.USER_NO_PERMISSION);
        }

        List<String> imageUrls = (images != null && !images.isEmpty())
                ? awsS3Facade.uploadFiles("notice/images", images, true)
                : new ArrayList<>();
        List<String> fileUrls = (files != null && !files.isEmpty())
                ? awsS3Facade.uploadFiles("notice/files", files, false)
                : new ArrayList<>();

        Notice notice = Notice.from(request, member, imageUrls, fileUrls);

        Notice savedNotice = noticeRepository.save(notice);

        return NoticeResponseDTO.Detail.from(savedNotice);
    }


    /** 단일 조회 */
    @Override
    public NoticeResponseDTO.Detail getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.NOTICE_NOT_EXIST));

        return NoticeResponseDTO.Detail.from(notice);
    }

    /** 전체 목록 조회 */
    @Override
    @Transactional(readOnly = true)
    public Page<NoticeResponseDTO.Detail> getNotices(PageRequest pageRequest) {
        return noticeRepository.findAll(pageRequest)
                .map(NoticeResponseDTO.Detail::from);
    }

    /** Type별 목록 조회 */
    @Override
    @Transactional(readOnly = true)
    public Page<NoticeResponseDTO.Detail> getNoticesByType(String type, PageRequest pageRequest) {
        return noticeRepository.findByType(NoticeType.from(type), pageRequest)
                .map(NoticeResponseDTO.Detail::from);
    }


    /** 삭제 */
    @Override
    public void deleteNotice(Long id, Member member){
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.NOTICE_NOT_EXIST));

        if (!"ROLE_ADMIN".equals(member.getRole().name()) && !"ROLE_OPS".equals(member.getRole().name())) {
            throw new CustomException(ErrorCode.USER_NO_PERMISSION);
        }

        List<String> imageUrls = notice.getImages();
        imageUrls.forEach(awsS3Facade::deleteFile);

        noticeRepository.deleteById(id);
    }


    /** 수정 */
    @Override
    public NoticeResponseDTO.Detail update(
            Long id, NoticeRequestDTO.Update request, Member member,
            List<String> imageUrls, List<MultipartFile> newImages,
            List<String> fileUrls, List<MultipartFile> newFiles) {

        if (!"ROLE_ADMIN".equals(member.getRole().name()) && !"ROLE_OPS".equals(member.getRole().name())) {
            throw new CustomException(ErrorCode.USER_NO_PERMISSION);
        }

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.NOTICE_NOT_EXIST));

        List<String> finalImageUrls = new ArrayList<>(imageUrls != null ? imageUrls : new ArrayList<>());
        List<String> finalFileUrls = new ArrayList<>(fileUrls != null ? fileUrls : new ArrayList<>());

        // 새 이미지 업로드
        if (newImages != null && !newImages.isEmpty()) {
            List<String> uploadedImageUrls = awsS3Facade.uploadFiles("notice/images", newImages, true);
            finalImageUrls.addAll(uploadedImageUrls);
        }

        // 새 파일 업로드
        if (newFiles != null && !newFiles.isEmpty()) {
            List<String> uploadedFileUrls = awsS3Facade.uploadFiles("notice/files", newFiles, false);
            finalFileUrls.addAll(uploadedFileUrls);
        }

        notice.update(request, finalImageUrls, finalFileUrls);

        return NoticeResponseDTO.Detail.from(notice);
    }

}
