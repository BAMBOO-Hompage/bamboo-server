package SMU.BAMBOO.Hompage.domain.knowledge.entity;

import SMU.BAMBOO.Hompage.domain.enums.KnowledgeType;
import SMU.BAMBOO.Hompage.domain.knowledgeComment.entity.KnowledgeComment;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "knowledge")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Knowledge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "knowledge_id")
    private Long knowledgeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private KnowledgeType type;

    @Column(nullable = false)
    private int views;

    @ElementCollection
    @CollectionTable(name = "main_activities_images", joinColumns = @JoinColumn(name = "main_activities_id"))
    @Column(name = "image_url")
    private List<String> image;

    @ElementCollection
    @CollectionTable(name = "main_activities_files", joinColumns = @JoinColumn(name = "main_activities_id"))
    @Column(name = "file_url")
    private List<String> file;

    @OneToMany(mappedBy = "knowledge", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KnowledgeComment> knowledgeComments = new ArrayList<>();
}
