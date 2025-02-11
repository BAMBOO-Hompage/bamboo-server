package SMU.BAMBOO.Hompage.domain.tag.entity;

import SMU.BAMBOO.Hompage.domain.mapping.libraryPostTag.LibraryPostTag;
import SMU.BAMBOO.Hompage.domain.tag.dto.TagRequestDTO;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tag")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(nullable = false, length = 50)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private List<LibraryPostTag> libraryPostTags = new ArrayList<>();

    public static Tag from(TagRequestDTO.Create request) {
        return Tag.builder()
                .name(request.name())
                .build();
    }
}
