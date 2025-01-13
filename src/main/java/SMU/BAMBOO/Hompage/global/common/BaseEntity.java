package SMU.BAMBOO.Hompage.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 해당 클래스를 직접 엔티티로 사용하지 않고, 다른 엔티티 클래스가 상속받아 공통 속성을 사용할 수 있도록 함
// 데이터베이스 테이블로 매핑되지 않으며, 이를 상속받은 클래스의 테이블에 필드가 포함됨
@MappedSuperclass
@Getter
// JPA 감사 기능을 활성화하기 위한 어노테이션
// AuditingEntityListener는 엔티티의 생성 및 수정 시간을 자동으로 기록
@EntityListeners(AuditingEntityListener.class)
// 엔티티의 공통속성 정의
public class BaseEntity {

    // 엔티티가 처음 저장될 때 createdAt 필드에 자동으로 날짜와 시간이 기록됨
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 엔티티가 수정될 때 updatedAt 필드에 자동으로 날짜와 시간이 업데이트됨
    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
}