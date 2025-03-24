package SMU.BAMBOO.Hompage.domain.knowledgeComment.entity;

import SMU.BAMBOO.Hompage.domain.knowledge.entity.Knowledge;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "knowledge_comment")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class KnowledgeComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "knowledge_comment_id")
    private Long knowledgeCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "knowledge_id", nullable = false)
    private Knowledge knowledge;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private KnowledgeComment parent;

    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private List<KnowledgeComment> children = new ArrayList<>();

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    public void updateContent(String content) {
        this.content = content;
    }

    public void softDelete() {
        this.isDeleted = true;
        this.content = "삭제된 댓글입니다.";
    }

    public static KnowledgeComment from(String content, Knowledge knowledge, Member member, KnowledgeComment parent) {
        return KnowledgeComment.builder()
                .content(content)
                .knowledge(knowledge)
                .member(member)
                .parent(parent)
                .build();
    }
}