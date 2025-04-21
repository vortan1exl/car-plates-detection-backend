package car.number.detection.repository;

import car.number.detection.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    void deleteByToken(String refreshToken);

    Optional<Token> findByToken(String token);

}