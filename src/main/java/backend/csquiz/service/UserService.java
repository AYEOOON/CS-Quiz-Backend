package backend.csquiz.service;

import backend.csquiz.entity.User;
import backend.csquiz.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

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
    public void saveScore(String nickname, int score){
        User user = userRepository.findByNickname(nickname)
                .orElse(new User()); // 새로운 사용자일 경우 생성
        user.setNickname(nickname);
        user.setScore(score);
        userRepository.save(user); // 점수 저장
    }

    public List<User> getRanking() {
        return userRepository.findAllByOrderByScoreDesc();
    }
}
