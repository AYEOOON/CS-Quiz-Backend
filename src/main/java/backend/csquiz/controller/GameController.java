package backend.csquiz.controller;

import backend.csquiz.dto.request.CheckAnswerRequestDTO;
import backend.csquiz.dto.request.StartGameRequestDTO;
import backend.csquiz.dto.response.CheckAnswerResponseDTO;
import backend.csquiz.dto.response.GameFinishResponseDTO;
import backend.csquiz.dto.response.GameStartResponseDTO;
import backend.csquiz.dto.response.QuestionResponseDTO;
import backend.csquiz.service.GameService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // 게임 시작
    @PostMapping("/start")
    public ResponseEntity<GameStartResponseDTO> startGame(@RequestBody StartGameRequestDTO request) {
        GameStartResponseDTO response = gameService.startGame(request.getNickname(), request.getDifficulty());
        return ResponseEntity.ok(response);
    }

    // 게임의 문제 조회
    @GetMapping("/{gameId}/questions")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionByGame(@PathVariable String gameId) {
        List<QuestionResponseDTO> response = gameService.getQuestionsByGame(gameId);
        return ResponseEntity.ok(response);
    }

    // 정답 확인
    @PostMapping("/{gameId}/answer")
    public ResponseEntity<CheckAnswerResponseDTO> checkAnswer(@PathVariable String gameId, @RequestBody CheckAnswerRequestDTO request) {
        boolean isCorrect = gameService.checkAnswer(gameId, request.getQuestionId(), request.getAnswer());
        return ResponseEntity.ok(new CheckAnswerResponseDTO(isCorrect));
    }

    // 게임 종료
    @PostMapping("/{gameId}/end")
    public ResponseEntity<GameFinishResponseDTO> finishGame(@PathVariable String gameId) {
        GameFinishResponseDTO response = gameService.finishGame(gameId);
        return ResponseEntity.ok(response);
    }
}