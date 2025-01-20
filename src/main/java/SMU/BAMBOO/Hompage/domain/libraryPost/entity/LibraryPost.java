package SMU.BAMBOO.Hompage.domain.libraryPost.entity;

import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostRequestDTO;
import SMU.BAMBOO.Hompage.domain.mapping.LibraryPostTag;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "library_post")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LibraryPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "library_post_id")
    private Long libraryPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @Column(nullable = false, length = 10)
    private String speaker;

    @Column(name = "paper_name", nullable = false, length = 50)
    private String paperName;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false, length = 50)
    private String topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String link;

    @Builder.Default
    @OneToMany(mappedBy = "libraryPost", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LibraryPostTag> libraryPostTags = new ArrayList<>();

    public void update(LibraryPostRequestDTO.Update request) {
        this.speaker = request.speaker();
        this.paperName = request.paperName();
        this.year = request.year();
        this.topic = request.topic();
        this.link = request.link();
        this.libraryPostTags = request.libraryPostTags();
    }

    /** 연관관계 편의 메서드 */
    public void addLibraryPostTag(LibraryPostTag libraryPostTag) {
        this.libraryPostTags.add(libraryPostTag);
        libraryPostTag.associateLibraryPost(this);
    }
}
