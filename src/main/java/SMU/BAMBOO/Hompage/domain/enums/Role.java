package SMU.BAMBOO.Hompage.domain.enums;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER(4),
    ROLE_MEMBER(3),
    ROLE_ADMIN(2),
    ROLE_OPS(1);

    private final int priority;

    Role(int priority) {
        this.priority = priority;
    }
}
/* 아기판다, 운영진, 임원진(operation) */
