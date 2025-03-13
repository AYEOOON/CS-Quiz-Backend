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
    public GameStartResponseDTO startGame(@RequestBody StartGameRequestDTO request) {
        return gameService.startGame(request.getNickname(), request.getDifficulty());
    }

    // 게임의 문제 조회
    @GetMapping("/{gameId}/questions")
    public List<QuestionResponseDTO> getQuestionByGame(@PathVariable String gameId) {
        return gameService.getQuestionsByGame(gameId);
    }

    // 정답 확인
    @PostMapping("/{gameId}/answer")
    public CheckAnswerResponseDTO checkAnswer(@PathVariable String gameId, @RequestBody CheckAnswerRequestDTO request) {
        boolean isCorrect = gameService.checkAnswer(gameId, request.getQuestionId(), request.getAnswer());
        return new CheckAnswerResponseDTO(isCorrect);
    }

    // 게임 종료
    @PostMapping("/{gameId}/end")
    public GameFinishResponseDTO finishGame(@PathVariable String gameId) {
        return gameService.finishGame(gameId);
    }

    // 게임 도중 나갈 시 게임 데이터 삭제
    @PostMapping("/{gameId}/leave")
    public ResponseEntity<String> leaveGame(@PathVariable String gameId){
        gameService.deleteGame(gameId);
        return ResponseEntity.ok("게임이 정상적으로 삭제되었습니다.");
    }
}