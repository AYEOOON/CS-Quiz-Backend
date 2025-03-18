package backend.csquiz.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import backend.csquiz.entity.Game;
import backend.csquiz.entity.Question;
import backend.csquiz.repository.GameRepository;
import backend.csquiz.repository.QuestionRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class GameServiceTest {
    @InjectMocks
    private GameRepository gameRepository;
    @Mock
    private QuestionService questionService;
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp(){
        Question testQuestion = new Question();
        testQuestion.setId(1L);
        testQuestion.setQuestion("다음 중 자료 구조 큐의 특징으로 옳은 것은?");
        testQuestion.setOptions(List.of("FIFO 구조", "임의 접근 구조", "LIFO 구조", "데이터 중복 방지 구조"));
        testQuestion.setAnswer("FIFO 구조");
        testQuestion.setDifficulty("Easy");
    }

    @Test
    @DisplayName("문제 저장 및 조회")
    void startGame_Success(){
        // given
        when(questionRepository.save(any(Question.class))).thenReturn(testQuestion);
        // when
        // then
    }
}
