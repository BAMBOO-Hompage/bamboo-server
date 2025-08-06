package SMU.BAMBOO.Hompage.domain.studyRecruitment.repository;

import SMU.BAMBOO.Hompage.domain.studyRecruitment.entity.StudyRecruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRecruitmentJpaRepository extends JpaRepository<StudyRecruitment, Long> {
    Page<StudyRecruitment> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
