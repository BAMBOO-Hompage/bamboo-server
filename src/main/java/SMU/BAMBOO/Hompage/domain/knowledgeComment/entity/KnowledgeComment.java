package SMU.BAMBOO.Hompage.domain.knowledgeComment.entity;

import SMU.BAMBOO.Hompage.domain.knowledge.entity.Knowledge;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "knowledge_comment")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class KnowledgeComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "knowledge_id")
    private Long knowledgeCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "knowledge_id", nullable = false)
    private Knowledge knowledge;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}
