package backend.csquiz.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import backend.csquiz.dto.response.GameStartResponseDTO;
import backend.csquiz.entity.Game;
import backend.csquiz.entity.Question;
import backend.csquiz.repository.GameRepository;
import backend.csquiz.repository.QuestionRepository;
import java.util.List;
import java.util.Optional;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserService userService;

    private Question question1;
    private Question question2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        question1 = new Question();
        question1.setId(1L);
        question1.setQuestion("자료 구조 큐의 특징은?");
        question1.setOptions(List.of("FIFO 구조", "임의 접근 구조", "LIFO 구조", "데이터 중복 방지 구조"));
        question1.setAnswer("FIFO 구조");
        question1.setDifficulty("Easy");

        question2 = new Question();
        question2.setId(2L);
        question2.setQuestion("스택의 특징은?");
        question2.setOptions(List.of("LIFO 구조", "FIFO 구조", "정렬 구조", "순환 구조"));
        question2.setAnswer("LIFO 구조");
        question2.setDifficulty("Easy");
    }

    @Test
    @DisplayName("게임 시작 - 성공적으로 문제 리스트를 반환한다")
    void startGame_Success() {
        // given
        String nickname = "testUser";
        String difficulty = "Easy";

        when(userService.isUserInGame(nickname)).thenReturn(false);
        when(userService.findByNickname(nickname)).thenReturn(Optional.empty());
        when(questionRepository.findAllByDifficulty(difficulty)).thenReturn(List.of(question1, question2));

        // Game 객체 저장 시 gameId 생성 및 반환
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> {
            Game savedGame = invocation.getArgument(0);
            savedGame.setGameId(UUID.randomUUID().toString());  // UUID로 gameId를 설정
            return savedGame;
        });

        // gameId로 게임 조회 시 반환
        when(gameRepository.findByGameId(anyString())).thenAnswer(invocation -> {
            String gameId = invocation.getArgument(0);
            return Optional.of(new Game(nickname, List.of(question1.getId(), question2.getId())));
        });

        // 문제 ID로 문제 조회
        when(questionRepository.findAllById(List.of(question1.getId(), question2.getId())))
                .thenReturn(List.of(question1, question2));

        doNothing().when(userService).markUserAsInGame(nickname);

        // when
        GameStartResponseDTO response = gameService.startGame(nickname, difficulty);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getGameQuestionList()).hasSize(2);
        assertThat(response.getGameQuestionList().getFirst().getQuestion()).isIn("자료 구조 큐의 특징은?", "스택의 특징은?");
    }

    @Test
    @DisplayName("게임 종료 - 게임이 종료 상태로 변경된다")
    void finishGame_Success() {
        // given
        String gameId = "testGameId"; // 이미 존재하는 게임 ID
        Game game = new Game("testUser", List.of(question1.getId(), question2.getId()));
        game.setGameId(gameId);

        when(gameRepository.findByGameId(gameId)).thenReturn(Optional.of(game));

        // when
        gameService.finishGame(gameId);

        // then
        assertThat(game.isFinished()).isTrue();
    }

    @Test
    @DisplayName("게임 시작 - 유저가 이미 게임을 진행 중일 때 게임을 시작할 수 없다")
    void startGame_UserAlreadyInGame() {
        // given
        String nickname = "testUser";
        String difficulty = "Easy";

        when(userService.isUserInGame(nickname)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> gameService.startGame(nickname, difficulty))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 사용 중인 닉네임입니다.");
    }

    @Test
    @DisplayName("게임 종료 - 유저 점수가 업데이트된다")
    void finishGame_UpdateScore() {
        // given
        String gameId = "testGameId";
        Game game = new Game("testUser", List.of(question1.getId(), question2.getId()));
        game.setGameId(gameId);
        game.setScore(10);  // 게임 진행 중 얻은 점수

        when(gameRepository.findByGameId(gameId)).thenReturn(Optional.of(game));

        // when
        gameService.finishGame(gameId);

        // then
        assertThat(game.getScore()).isEqualTo(10);
    }
}
