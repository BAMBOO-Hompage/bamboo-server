package SMU.BAMBOO.Hompage.domain.mapping.memberStudy.repository;

import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.entity.MemberStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberStudyRepository extends JpaRepository<MemberStudy, Long> {
    Optional<MemberStudy> findByMember_MemberIdAndStudy_StudyId(Long memberId, Long studyId);
}
