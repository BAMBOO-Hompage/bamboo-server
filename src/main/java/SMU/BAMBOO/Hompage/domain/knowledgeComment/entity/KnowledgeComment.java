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

    @Column(name = "WRITER_ID", nullable = false)
    private Long writerId;

    @Column(name = "WRITER_NAME", nullable = false)
    private String writerName;

    @Column(name = "WRITER_MAJOR", nullable = false)
    private String writerMajor;

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
                .writerId(member.getMemberId())
                .writerName(member.getName())
                .writerMajor(member.getMajor())
                .parent(parent)
                .build();
    }
}