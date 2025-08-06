package SMU.BAMBOO.Hompage.domain.studyRecruitment.repository;

import SMU.BAMBOO.Hompage.domain.studyRecruitment.entity.StudyRecruitment;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyRecruitmentRepositoryImpl implements StudyRecruitmentRepository {

    private final StudyRecruitmentJpaRepository studyRecruitmentJpaRepository;

    @Override
    public Optional<StudyRecruitment> findById(Long id) {
        return studyRecruitmentJpaRepository.findById(id);
    }

    @Override
    public StudyRecruitment getById(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(ErrorCode.STUDY_RECRUITMENT_NOT_EXIST));
    }

    @Override
    public StudyRecruitment save(StudyRecruitment studyRecruitment) {
        return studyRecruitmentJpaRepository.save(studyRecruitment);
    }

    @Override
    public void deleteById(Long id) {
        studyRecruitmentJpaRepository.deleteById(id);
    }

    @Override
    public Page<StudyRecruitment> findAll(Pageable pageable) {
        return studyRecruitmentJpaRepository.findAll(pageable);
    }

    @Override
    public Page<StudyRecruitment> findAllByTitle(String title, Pageable pageable) {
        return studyRecruitmentJpaRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
}
