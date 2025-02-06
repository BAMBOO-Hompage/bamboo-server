package SMU.BAMBOO.Hompage.domain.notice.repository;

import SMU.BAMBOO.Hompage.domain.notice.entity.Notice;

public interface NoticeRepository {
    Notice save(Notice notice);
}
