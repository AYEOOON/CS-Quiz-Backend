package backend.csquiz.repository;

import backend.csquiz.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname); // 닉네임으로 사용자 조회

    // 점수 내림차순으로 정렬하여 모든 사용자 조회
    List<User> findAllByOrderByScoreDesc();
}
