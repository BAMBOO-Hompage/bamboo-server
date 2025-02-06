package SMU.BAMBOO.Hompage.domain.notice.entity;

import SMU.BAMBOO.Hompage.domain.enums.NoticeType;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeRequestDTO;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notice")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private NoticeType type;

    @ElementCollection
    @CollectionTable(name = "notice_images", joinColumns = @JoinColumn(name = "notice_id"))
    @Column(name = "image_url")
    private List<String> images;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "notice_files", joinColumns = @JoinColumn(name = "notice_id"))
    @Column(name = "file_url")
    private List<String> files = new ArrayList<>();

    public static Notice from(NoticeRequestDTO.Create request, Member member, List<String> images, List<String> files) {
        return Notice.builder()
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .type(NoticeType.valueOf(request.getType()))
                .images(images != null ? new ArrayList<>(images) : new ArrayList<>())
                .files(files != null ? new ArrayList<>(files) : new ArrayList<>())
                .build();
    }

    public void update(NoticeRequestDTO.Update request,  List<String> newImages, List<String> newFiles) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.type = NoticeType.valueOf(request.getType());
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
