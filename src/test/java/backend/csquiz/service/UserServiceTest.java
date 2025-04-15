package backend.csquiz.service;

import backend.csquiz.entity.User;
import backend.csquiz.repository.UserRepository;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setNickname("testUser");
        user.setScore(100);
    }

    @Test
    @DisplayName("닉네임으로 사용자 조회 - 성공")
    void findByNickname_Success() {
        // given
        when(userRepository.findByNickname("testUser")).thenReturn(Optional.of(user));

        // when
        Optional<User> foundUser = userService.findByNickname("testUser");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getNickname()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("유저가 게임에 참여 중일 때 true 반환")
    void isUserInGame_UserInGame() {
        // given
        when(redisTemplate.hasKey("game:user:testUser")).thenReturn(true);

        // when
        boolean isInGame = userService.isUserInGame("testUser");

        // then
        assertThat(isInGame).isTrue();
    }

    @Test
    @DisplayName("유저가 게임에 참여 중이지 않으면 false 반환")
    void isUserInGame_UserNotInGame() {
        // given
        when(redisTemplate.hasKey("game:user:testUser")).thenReturn(false);

        // when
        boolean isInGame = userService.isUserInGame("testUser");

        // then
        assertThat(isInGame).isFalse();
    }

    @Test
    @DisplayName("게임 시작 시 유저를 Redis에 저장")
    void markUserAsInGame() {
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // when
        userService.markUserAsInGame("testUser");

        // then
        verify(valueOperations, times(1)).set(eq("game:user:testUser"), eq("IN_GAME"), eq(10L), eq(TimeUnit.MINUTES));
    }

    @Test
    @DisplayName("게임 종료 시 Redis에서 유저 삭제")
    void removeUserFromGame() {
        // when
        userService.removeUserFromGame("testUser");

        // then
        verify(redisTemplate, times(1)).delete("game:user:testUser");
    }

    @Test
    @DisplayName("사용자 점수 저장 - 사용자가 이미 존재할 때 점수 추가")
    void saveScore_UserExists() {
        // given
        when(userRepository.findByNickname("testUser")).thenReturn(Optional.of(user));

        // when
        userService.saveScore("testUser", 50);

        // then
        assertThat(user.getScore()).isEqualTo(150);  // 기존 점수 100에 50점을 추가
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("랭킹 조회 - 점수순으로 사용자 목록 반환")
    void getRanking() {
        // given
        when(userRepository.findAllByOrderByScoreDesc()).thenReturn(List.of(user));

        // when
        List<User> ranking = userService.getRanking();

        // then
        assertThat(ranking).hasSize(1);
        assertThat(ranking.get(0).getNickname()).isEqualTo("testUser");
    }
}
