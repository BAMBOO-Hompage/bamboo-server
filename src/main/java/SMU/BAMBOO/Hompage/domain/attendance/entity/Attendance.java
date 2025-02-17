package SMU.BAMBOO.Hompage.domain.attendance.entity;

import SMU.BAMBOO.Hompage.domain.enums.AttendanceStatus;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.studyWeek.entity.StudyWeek;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attendance")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_week_id", nullable = false)
    private StudyWeek studyWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    public static Attendance create(StudyWeek studyWeek, Member member, AttendanceStatus status) {
        return Attendance.builder()
                .studyWeek(studyWeek)
                .member(member)
                .status(status)
                .build();
    }

    public void updateStatus(AttendanceStatus newStatus) {
        this.status = newStatus;
    }
}
