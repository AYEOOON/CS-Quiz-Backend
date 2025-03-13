package backend.csquiz.service;

import backend.csquiz.entity.User;
import backend.csquiz.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 닉네임으로 사용자 조회
    public Optional<User> findByNickname(String nickname){
        return userRepository.findByNickname(nickname);
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
