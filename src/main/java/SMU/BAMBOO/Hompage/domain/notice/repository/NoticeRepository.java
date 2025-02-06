package SMU.BAMBOO.Hompage.domain.notice.repository;

import SMU.BAMBOO.Hompage.domain.enums.NoticeType;
import SMU.BAMBOO.Hompage.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NoticeRepository {
    Notice save(Notice notice);
    Optional<Notice> findById(Long id);
    void deleteById(Long id);
    Page<Notice> findByType(NoticeType type, Pageable pageable);
    Page<Notice> findAll(Pageable pageable);
}
