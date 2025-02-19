package SMU.BAMBOO.Hompage.domain.award.repository;

import SMU.BAMBOO.Hompage.domain.award.entity.Award;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AwardRepositoryImpl implements AwardRepository {

    private final AwardJpaRepository awardJpaRepository;

    @Override
    public Award getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.AWARD_NOT_EXIST));
    }

    @Override
    public Optional<Award> findById(Long id) {
        return awardJpaRepository.findById(id);
    }

    @Override
    public List<Award> findAll() {
        return awardJpaRepository.findAll();
    }

    @Override
    public List<Award> findByBatch(int batch) {
        return awardJpaRepository.findByBatch(batch);
    }

    @Override
    public Award save(Award award) {
        return awardJpaRepository.save(award);
    }

    @Override
    public void delete(Long id) {
        awardJpaRepository.deleteById(id);
    }
}
