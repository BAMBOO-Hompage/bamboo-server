package SMU.BAMBOO.Hompage.domain.notice.repository;

import SMU.BAMBOO.Hompage.domain.enums.NoticeType;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeResponseDTO;
import SMU.BAMBOO.Hompage.domain.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepository {

    private final NoticeJpaRepository noticeJpaRepository;

    @Override
    public Notice save(Notice notice){
        return noticeJpaRepository.save(notice);
    }

    @Override
    public Optional<Notice> findById(Long id){
        return noticeJpaRepository.findById(id);
    }

    @Override
    public void deleteById(Long id){
        noticeJpaRepository.deleteById(id);
    }

    @Override
    public Page<Notice> findByType(NoticeType type, Pageable pageable){
        return noticeJpaRepository.findByType(type, pageable);
    }

    @Override
    public Page<Notice> findAll(Pageable pageable){
        return noticeJpaRepository.findAll(pageable);
    }

}
