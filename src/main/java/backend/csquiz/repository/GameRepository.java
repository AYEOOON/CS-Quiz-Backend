package backend.csquiz.repository;

import backend.csquiz.entity.Game;
import backend.csquiz.entity.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByGameId(String gameId); // 게임 ID로 게임 세션 조회
}
