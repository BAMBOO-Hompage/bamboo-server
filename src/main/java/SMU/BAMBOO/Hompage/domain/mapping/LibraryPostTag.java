package SMU.BAMBOO.Hompage.domain.mapping;

import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "library_post_tag")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LibraryPostTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "library_post_tag_id")
    private Long libraryPostTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_post_id", nullable = false)
    private LibraryPost libraryPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

}
