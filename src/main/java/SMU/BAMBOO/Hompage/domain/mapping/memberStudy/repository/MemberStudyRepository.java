package SMU.BAMBOO.Hompage.domain.mapping.memberStudy.repository;

import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.entity.MemberStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberStudyRepository extends JpaRepository<MemberStudy, Long> {
    boolean existsByStudyIdAndMemberId(Long studyId, Long memberId);
    List<MemberStudy> findByStudyId(Long studyId);
}
