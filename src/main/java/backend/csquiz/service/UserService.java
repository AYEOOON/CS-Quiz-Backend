package backend.csquiz.service;

import backend.csquiz.entity.User;
import backend.csquiz.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public final StringRedisTemplate redisTemplate;


    public UserService(UserRepository userRepository, StringRedisTemplate redisTemplate) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    // 닉네임으로 사용자 조회
    public Optional<User> findByNickname(String nickname){
        return userRepository.findByNickname(nickname);
    }

    // 게임 시작 시 닉네임 중복 체크
    public boolean isUserInGame(String nickname){
        return redisTemplate.hasKey("game:user:" + nickname);
    }

    // 게임 시작 시 닉네임을 Redis 에 저장
    public void markUserAsInGame(String nickname){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("game:user:" + nickname, "IN_GAME", 10, TimeUnit.MINUTES); // TTL 10분 설정
    }

    // 게임 종료 시 Redis 에서 삭제
    public void removeUserFromGame(String nickname){
        redisTemplate.delete("game:user:" + nickname);

    }

    // 사용자 점수 저장
    @Transactional
    public void saveScore(String nickname, int score) {
        User user = userRepository.findByNickname(nickname)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setNickname(nickname);
                    newUser.setScore(0); // 초기 점수 0 설정
                    return userRepository.save(newUser);
                });

        // 점수 누적
        user.setScore(user.getScore() + score);
        userRepository.save(user);
    }

    public List<User> getRanking() {
        return userRepository.findAllByOrderByScoreDesc();
    }
}
