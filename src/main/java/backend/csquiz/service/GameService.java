package backend.csquiz.service;

import backend.csquiz.dto.response.GameFinishResponseDTO;
import backend.csquiz.dto.response.GameStartResponseDTO;
import backend.csquiz.dto.response.QuestionResponseDTO;
import backend.csquiz.entity.Game;
import backend.csquiz.entity.Question;
import backend.csquiz.repository.GameRepository;
import backend.csquiz.repository.QuestionRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final QuestionRepository questionRepository;
    private final UserService userService;

    // 난이도에 따른 점수 매핑
    private static final Map<String, Integer> SCORE_MAP = Map.of(
            "Easy", 10,
            "Normal", 20,
            "Hard", 30
    );

    public GameService(GameRepository gameRepository, QuestionRepository questionRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.questionRepository = questionRepository;
        this.userService = userService;
    }

    // 게임 시작
    public GameStartResponseDTO startGame(String nickname, String difficulty) {
        // 닉네임 중복 확인
        if(userService.isUserInGame(nickname) || userService.findByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        List<Long> questionIds = getQuestionsByDifficulty(difficulty).stream()
                .map(Question::getId)
                .collect(Collectors.toList());

        // 문제를 무작위로 섞음
        Collections.shuffle(questionIds);

        // 새로운 게임 생성
        Game game = new Game(nickname, questionIds);
        gameRepository.save(game);

        // 게임 시작 시 사용자 상태 업데이트 (게임에 참여 중임을 Redis에 기록)
        userService.markUserAsInGame(nickname);

        return new GameStartResponseDTO(game.getGameId(), getQuestionsByGame(game.getGameId()));
    }

    // 난이도에 따른 문제 가져오기
    private List<Question> getQuestionsByDifficulty(String difficulty) {
        if ("random".equalsIgnoreCase(difficulty)) {
            List<Question> allQuestions = new ArrayList<>();
            allQuestions.addAll(questionRepository.findAllByDifficulty("Easy"));
            allQuestions.addAll(questionRepository.findAllByDifficulty("Normal"));
            allQuestions.addAll(questionRepository.findAllByDifficulty("Hard"));
            return allQuestions.subList(0, 20);
        } else {
            return questionRepository.findAllByDifficulty(difficulty);
        }
    }

    // 정답 확인
    @Transactional
    public boolean checkAnswer(String gameId, Long questionId, String userAnswer){
        Game game = findGameById(gameId);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 질문입니다."));

        // 정답 확인
        boolean isCorrect = question.getAnswer().equals(userAnswer);
        if(isCorrect){
            int score = SCORE_MAP.getOrDefault(question.getDifficulty(), 0); // 난이도에 따른 점수 계산
            game.setScore(game.getScore() + score);
        }
        return isCorrect;
    }

    // 게임 ID로 게임 찾기
    private Game findGameById(String gameId) {
        return gameRepository.findByGameId(gameId)
                .orElseThrow(()-> new IllegalArgumentException("잘못된 게임 ID입니다."));
    }

    // 게임 종료 및 최종 점수 반환
    @Transactional
    public GameFinishResponseDTO finishGame(String gameId) {
        Game game = gameRepository.findByGameId(gameId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게임입니다."));

        if (!game.isFinished()) {
            game.finish(); // 게임 종료 상태 변경
            userService.saveScore(game.getNickname(), game.getScore());
        }

        // 게임 종료 후 사용자 상태 처리
        userService.removeUserFromGame(game.getNickname());

        return new GameFinishResponseDTO(game.getNickname(), game.getScore());
    }

    // 게임에 출제된 문제 조회
    public List<QuestionResponseDTO> getQuestionsByGame(String gameId) {
        Game game = gameRepository.findByGameId(gameId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게임입니다."));

        // game에서 ID들만 가져온 후, 해당 ID들로 문제 조회
        List<Question> questions = questionRepository.findAllById(game.getQuestionIds());

        // 문제들을 QuestionRepositoryDTO로 변환하여 반환
        return questions.stream()
                .map(question -> new QuestionResponseDTO(
                        question.getId(),
                        question.getQuestion(),
                        question.getAnswer(),
                        question.getDifficulty(),
                        question.getOptions()
                )).collect(Collectors.toList());
    }
}
