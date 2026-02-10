package SMU.BAMBOO.Hompage.domain.faq.entity;

import SMU.BAMBOO.Hompage.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * FAQ 엔티티 - DB의 'faq' 테이블과 매핑됨
 * 
 * @Entity: 이 클래스가 DB 테이블이라는 표시
 * @Table: 실제 DB 테이블 이름 지정
 * @Builder: 객체를 쉽게 생성하기 위한 빌더 패턴
 * @Getter: 모든 필드의 getter 메서드 자동 생성
 * 
 * BaseEntity를 상속받아서 createdAt, updatedAt 자동 관리
 */
@Entity
@Table(name = "faq")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자 (JPA용, 외부 사용 막음)
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // 모든 필드 생성자 (Builder용)
@Getter
public class Faq extends BaseEntity {

    /**
     * 기본키 (Primary Key)
     * @Id: 이 필드가 PK라는 표시
     * @GeneratedValue: 자동 증가 (1, 2, 3...)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Long faqId;

    /**
     * 질문 내용
     * nullable = false: 필수 입력값 (NULL 불가)
     * length = 500: 최대 500자
     */
    @Column(nullable = false, length = 500)
    private String question;

    /**
     * 답변 내용
     * columnDefinition = "TEXT": 긴 텍스트 저장 가능
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    /**
     * 카테고리 (분류)
     * 예: "가입", "스터디", "출석", "기타"
     */
    @Column(nullable = false, length = 50)
    private String category;

    /**
     * 표시 순서
     * FAQ 목록에서 보여줄 순서 (1, 2, 3...)
     * 숫자가 작을수록 위에 표시
     */
    @Column(nullable = false)
    private Integer displayOrder;

    /**
     * FAQ 수정 메서드
     * Entity의 값을 변경할 때는 setter 대신 이런 메서드를 사용
     * (의도를 명확하게 표현하기 위해)
     */
    public void updateFaq(String question, String answer, String category, Integer displayOrder) {
        if (question != null) this.question = question;
        if (answer != null) this.answer = answer;
        if (category != null) this.category = category;
        if (displayOrder != null) this.displayOrder = displayOrder;
    }
}
