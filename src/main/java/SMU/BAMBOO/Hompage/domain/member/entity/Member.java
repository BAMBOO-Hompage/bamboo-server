package SMU.BAMBOO.Hompage.domain.member.entity;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.inventory.entity.Inventory;
import SMU.BAMBOO.Hompage.domain.mapping.MemberStudy;
import SMU.BAMBOO.Hompage.domain.member.dto.request.MemberSignUpDto;
import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@SQLDelete(sql = "UPDATE member SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE member_id = ?")
@SQLRestriction("is_deleted = false")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "student_id", nullable = false)
    @Size(min=9, max=9)
    private String studentId;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String pw;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String major;

    @Column(nullable = false)
    @Size(min=11, max=11)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private Role role;

    private String profileImageUrl;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column
    private LocalDateTime deletedAt;

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<MemberStudy> memberStudies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Inventory> inventories = new ArrayList<>();

    public static Member from(MemberSignUpDto request, BCryptPasswordEncoder encoder) {
        return Member.builder()
                .studentId(request.studentId())
                .email(request.email())
                .pw(encoder.encode(request.password()))
                .name(request.name())
                .major(request.major())
                .phone(request.phoneNumber())
                .role(Role.ROLE_USER)
                .build();
    }

    public void updateProfile(String phoneNumber,
                              String profileImageUrl) {
        this.phone = phoneNumber;
        this.profileImageUrl = profileImageUrl;
    }

    public void setBasicProfileImage() {
        this.profileImageUrl = null;
    }

    public void updatePw(String pw) {
        this.pw = pw;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    /** 비활성화 - Soft Delete */
    public void deactivate() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 연관 관계 편의 메서드
     */
    public void addMemberStudy(MemberStudy memberStudy) {
        this.memberStudies.add(memberStudy);
        memberStudy.associateMember(this);
    }
}
