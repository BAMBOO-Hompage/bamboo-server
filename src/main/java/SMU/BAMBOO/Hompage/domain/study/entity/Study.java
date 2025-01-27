package SMU.BAMBOO.Hompage.domain.study.entity;

import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.mapping.MemberStudy;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Study extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long studyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private int cohort;

    @Builder.Default
    @Column(name = "is_book", nullable = false)
    private Boolean isBook = false;

    @Column(nullable = false)
    private int section;

    @Column(name = "study_master", nullable = false)
    private String studyMaster;

    @Builder.Default
    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberStudy> memberStudies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    private List<Inventory> inventories = new ArrayList<>();
}
