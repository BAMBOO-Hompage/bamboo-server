package SMU.BAMBOO.Hompage.domain.studyRecruitment.repository;

import SMU.BAMBOO.Hompage.domain.studyRecruitment.entity.StudyRecruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface StudyRecruitmentRepository {
    Optional<StudyRecruitment> findById(Long id);
    StudyRecruitment getById(Long id);
    StudyRecruitment save(StudyRecruitment studyRecruitment);
    void deleteById(Long id);
    Page<StudyRecruitment> findAll(Pageable pageable);
    Page<StudyRecruitment> findAllByTitle(String title, Pageable pageable);
}
