package SMU.BAMBOO.Hompage.domain.knowledge.entity;

import SMU.BAMBOO.Hompage.domain.enums.KnowledgeType;
import SMU.BAMBOO.Hompage.domain.knowledge.dto.KnowledgeRequestDTO;
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

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private KnowledgeType type;

    @Builder.Default
    @Column(nullable = false)
    private int views = 0;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "knowledge_images", joinColumns = @JoinColumn(name = "knowledge_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "knowledge_files", joinColumns = @JoinColumn(name = "knowledge_id"))
    @Column(name = "file_url")
    private List<String> files = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "knowledge", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KnowledgeComment> knowledgeComments = new ArrayList<>();

    public static Knowledge from(KnowledgeRequestDTO.Create request, Member member, List<String> images, List<String> files) {
        return Knowledge.builder()
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .views(0)
                .images(images != null ? new ArrayList<>(images) : new ArrayList<>())
                .files(files != null ? new ArrayList<>(files) : new ArrayList<>())
                .build();
    }

    public void update(KnowledgeRequestDTO.Update request,  List<String> newImages, List<String> newFiles) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.type = request.getType();
        if (newImages != null) {
            this.images.clear();
            this.images.addAll(newImages);
        }
        if (newFiles != null) {
            this.files.clear();
            this.files.addAll(newFiles);
        }
    }
}

