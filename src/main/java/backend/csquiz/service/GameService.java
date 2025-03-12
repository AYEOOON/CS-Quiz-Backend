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
        if(userService.findByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 새로운 유저 등록(초기 점수 0)
        userService.saveScore(nickname, 0);

        List<Long> questionIds = getQuestionsByDifficulty(difficulty).stream()
                .map(Question::getId)
                .collect(Collectors.toList());

        // 문제를 무작위로 섞은 후, 필요한 문제만 선택
        Collections.shuffle(questionIds);
        questionIds = questionIds.subList(0, Math.min(10, questionIds.size()));

        // 새로운 게임 생성
        Game game = new Game(nickname, questionIds);
        gameRepository.save(game);

        return new GameStartResponseDTO(game.getGameId(), getQuestionsByGame(game.getGameId()));
    }

    // 난이도에 따른 문제 가져오기
    private List<Question> getQuestionsByDifficulty(String difficulty) {
        if ("random".equalsIgnoreCase(difficulty)) {
            List<Question> allQuestions = new ArrayList<>();
            allQuestions.addAll(questionRepository.findAllByDifficulty("easy"));
            allQuestions.addAll(questionRepository.findAllByDifficulty("normal"));
            allQuestions.addAll(questionRepository.findAllByDifficulty("hard"));
            return allQuestions;
        } else {
            return questionRepository.findAllByDifficulty(difficulty);
        }
    }

    // 정답 확인
    @Transactional
    public boolean checkAnswer(String gameId, Long questionId, String userAnswer){
        System.out.println("사용자 답: "+userAnswer);
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
        Game game = findGameById(gameId);
        userService.saveScore(game.getNickname(), game.getScore());
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
                        question.getOptions(),
                        false
                )).collect(Collectors.toList());
    }

    public QuestionResponseDTO getNextQuestion(String gameId) {
        // 게임 ID로 게임 정보를 조회
        Game game = findGameById(gameId);

        // 게임에 저장된 문제 리스트에서 현재 진행 중인 문제 찾기
        int currentIndex = game.getCurrentQuestionIndex();

        // 모든 문제를 다 풀었으면 null 반환
        if (currentIndex >= game.getQuestionIds().size()) {
            return new QuestionResponseDTO(null, "모든 문제를 완료하였습니다.", null, null, null, true);
        }

        // 문제 ID를 이용해 DB에서 직접 조회
        Long questionId = game.getQuestionIds().get(currentIndex);

        // 다음 문제 가져오기
        Question nextQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));

        // 현재 문제 인덱스 업데이트
        game.setCurrentQuestionIndex(currentIndex+1);
        gameRepository.save(game);

        // DTO로 변환하여 반환
        return new QuestionResponseDTO(
                nextQuestion.getId(),
                nextQuestion.getQuestion(),
                nextQuestion.getAnswer(),
                nextQuestion.getDifficulty(),
                nextQuestion.getOptions(),
                false // 마지막 문제가 아님
        );
    }
}
