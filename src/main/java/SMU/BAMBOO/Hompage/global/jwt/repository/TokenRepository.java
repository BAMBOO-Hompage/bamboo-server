package SMU.BAMBOO.Hompage.global.jwt.repository;

import SMU.BAMBOO.Hompage.global.jwt.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByStudentId(String studentId);

}