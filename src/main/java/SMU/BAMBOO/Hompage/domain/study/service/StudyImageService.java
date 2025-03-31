package SMU.BAMBOO.Hompage.domain.study.service;

import org.springframework.web.multipart.MultipartFile;

public interface StudyImageService {
    String getStudyWeekImage(Long studyId, int weekNumber);
    void updateStudyWeekImage(Long studyId, int weekNumber, MultipartFile image);
    void deleteStudyWeekImage(Long studyId, int weekNumber);
}
