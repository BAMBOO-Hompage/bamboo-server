package SMU.BAMBOO.Hompage.domain.subject.repository;

import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubjectJpaRepositoryImpl implements SubjectRepository {

    private final SubjectJpaRepository subjectJpaRepository;
    @Override
    public Optional<Subject> findById(Long id) {
        return subjectJpaRepository.findById(id);
    }

    @Override
    public Optional<Subject> findByName(String name) {
        return subjectJpaRepository.findByName(name);
    }

    @Override
    public List<Subject> findAll() {
        return subjectJpaRepository.findAll();
    }

    @Override
    public Subject save(Subject subject) {
        return subjectJpaRepository.save(subject);
    }

    @Override
    public void deleteById(Long id) {
        subjectJpaRepository.deleteById(id);
    }
}
