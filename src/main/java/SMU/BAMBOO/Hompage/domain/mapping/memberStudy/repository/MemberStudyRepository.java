package SMU.BAMBOO.Hompage.domain.mapping.memberStudy.repository;

import SMU.BAMBOO.Hompage.domain.mapping.memberStudy.entity.MemberStudy;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStudyRepository extends JpaRepository<MemberStudy, Long> {
    boolean existsByStudyAndMember(Study study, Member member);
}
