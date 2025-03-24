package SMU.BAMBOO.Hompage.domain.libraryPostComment.entity;

import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "library_post_comment")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LibraryPostComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long libraryPostCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_post_id")
    private LibraryPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private LibraryPostComment parent;

    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private List<LibraryPostComment> children = new ArrayList<>();

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

    public static LibraryPostComment from(String content, LibraryPost post, Member member, LibraryPostComment parent) {
        return LibraryPostComment.builder()
                .content(content)
                .post(post)
                .member(member)
                .parent(parent)
                .build();
    }
}

