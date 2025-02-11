package SMU.BAMBOO.Hompage.domain.libraryPost.entity;

import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostRequestDTO;
import SMU.BAMBOO.Hompage.domain.mapping.libraryPostTag.LibraryPostTag;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
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

    @Column(name = "paper_name", nullable = false, length = 200)
    private String paperName;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false, length = 200)
    private String topic;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String link;

    @Builder.Default
    @OneToMany(mappedBy = "libraryPost", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LibraryPostTag> libraryPostTags = new ArrayList<>();

    public void updateBasicFields(LibraryPostRequestDTO.Update request) {
        this.paperName = request.paperName();
        this.year = request.year();
        this.topic = request.topic();
        this.content = request.content();
        this.link = request.link();
    }

    /** 연관관계 편의 메서드 */
    public void addLibraryPostTag(LibraryPostTag libraryPostTag) {
        this.libraryPostTags.add(libraryPostTag);
        libraryPostTag.associateLibraryPost(this);
    }

    /** 기존 LibraryPost 에 새로운 태그를 추가하는 메서드 */
    public void addTags(List<Tag> tags) {
        tags.forEach(tag -> {
            LibraryPostTag libraryPostTag = LibraryPostTag.builder()
                    .tag(tag)
                    .libraryPost(this)
                    .build();
            this.addLibraryPostTag(libraryPostTag);
        });
    }

    /** libraryPostTags 를 새로 설정하는 메서드 */
    public void setTags(List<Tag> newTags) {
        // 기존 태그 제거
        this.libraryPostTags.clear();

        // 새로운 태그 리스트 추가
        newTags.forEach(tag -> {
            LibraryPostTag libraryPostTag = LibraryPostTag.builder()
                    .tag(tag)
                    .libraryPost(this)
                    .build();
            this.addLibraryPostTag(libraryPostTag);
        });
    }
}
