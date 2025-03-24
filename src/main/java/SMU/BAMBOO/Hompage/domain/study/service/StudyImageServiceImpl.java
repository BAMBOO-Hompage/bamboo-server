package SMU.BAMBOO.Hompage.domain.study.service;

import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.study.repository.StudyRepository;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;
import SMU.BAMBOO.Hompage.domain.studyWeek.repository.StudyWeekRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyImageServiceImpl implements StudyImageService {

    private final StudyRepository studyRepository;
    private final StudyWeekRepository studyWeekRepository;
    private final AwsS3Facade awsS3Facade;

    /**
     * 주차별 이미지 조회
     */
    @Override
    public String getStudyWeekImage(Long studyId, int weekNumber) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_EXIST));
        StudyWeek studyWeek = studyWeekRepository.findByStudyAndWeek(study, weekNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_WEEK_NOT_EXIST));
        return studyWeek.getImageUrl();
    }

    /**
     * 주차별 이미지 등록/업데이트
     */
    @Override
    @Transactional
    public void updateStudyWeekImage(Long studyId, int weekNumber, MultipartFile image) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_EXIST));

        // 주차별 정보를 조회하거나 없으면 새로 생성
        StudyWeek studyWeek = studyWeekRepository.findByStudyAndWeek(study, weekNumber)
                .orElseGet(() -> createNewStudyWeek(study, weekNumber));

        // 기존 이미지 삭제 (기존 URL이 있는 경우)
        if (studyWeek.getImageUrl() != null) {
            awsS3Facade.deleteFile(studyWeek.getImageUrl());
        }

        // 새 이미지 업로드
        String fileUrl = awsS3Facade.uploadFile("study-week/images", image, true);
        studyWeek.updateWeekImage(fileUrl);
    }

    /**
     * 주차가 없는 경우 새로 생성하는 메서드
     */
    private StudyWeek createNewStudyWeek(Study study, int weekNumber) {
        return studyWeekRepository.save(StudyWeek.create(study, weekNumber));
    }
}
