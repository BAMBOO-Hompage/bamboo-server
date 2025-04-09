package SMU.BAMBOO.Hompage.domain.subject.repository.dto;

import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubjectWeek {
    private Subject subject;
    private Integer week;
}
