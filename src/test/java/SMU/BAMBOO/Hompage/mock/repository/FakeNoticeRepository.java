package SMU.BAMBOO.Hompage.mock.repository;

import SMU.BAMBOO.Hompage.domain.enums.NoticeType;
import SMU.BAMBOO.Hompage.domain.notice.entity.Notice;
import SMU.BAMBOO.Hompage.domain.notice.repository.NoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeNoticeRepository implements NoticeRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<Notice> data = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Notice save(Notice notice) {
        if (notice.getNoticeId() == null || notice.getNoticeId() == 0) {
            Notice newNotice = Notice.builder()
                    .noticeId(autoGeneratedId.incrementAndGet())
                    .member(notice.getMember())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .type(notice.getType())
                    .images(new ArrayList<>(notice.getImages() != null ? notice.getImages() : new ArrayList<>()))
                    .files(new ArrayList<>(notice.getFiles() != null ? notice.getFiles() : new ArrayList<>()))
                    .build();


            data.add(newNotice);
            return newNotice;
        } else {
            deleteById(notice.getNoticeId());
            data.add(notice);
            return notice;
        }
    }

    @Override
    public Optional<Notice> findById(Long id) {
        return data.stream()
                .filter(notice -> notice.getNoticeId().equals(id))
                .findAny();
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(notice -> notice.getNoticeId().equals(id));
    }

    @Override
    public Page<Notice> findByType(NoticeType type, Pageable pageable) {
        List<Notice> filteredNotices = new ArrayList<>();
        for (Notice notice : data) {
            if (notice.getType() == type) {
                filteredNotices.add(notice);
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredNotices.size());
        List<Notice> pagedNotices = filteredNotices.subList(start, end);

        return new PageImpl<>(pagedNotices, pageable, filteredNotices.size());
    }

    @Override
    public Page<Notice> findAll(Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), data.size());
        List<Notice> pagedNotices = data.subList(start, end);

        return new PageImpl<>(pagedNotices, pageable, data.size());
    }
}
