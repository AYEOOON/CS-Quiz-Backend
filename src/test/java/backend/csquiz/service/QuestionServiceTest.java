package backend.csquiz.service;

import backend.csquiz.dto.response.QuestionResponseDTO;
import backend.csquiz.entity.Question;
import backend.csquiz.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    private Question question1;
    private Question question2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Question 객체들 초기화
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
    @DisplayName("모든 문제를 조회했을 때, 문제 목록이 반환된다.")
    void getAllQuestions_ReturnsListOfQuestions() {
        // given
        when(questionRepository.findAll()).thenReturn(List.of(question1, question2));

        // when
        List<QuestionResponseDTO> result = questionService.getAllQuestions();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("자료 구조 큐의 특징은?", result.get(0).getQuestion());
        assertEquals("스택의 특징은?", result.get(1).getQuestion());
        verify(questionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("주어진 ID로 문제를 조회했을 때, 해당 문제가 반환된다.")
    void getQuestionById_ReturnsQuestion() {
        // given
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question1));

        // when
        QuestionResponseDTO result = questionService.getQuestionById(1L);

        // then
        assertNotNull(result);
        assertEquals("자료 구조 큐의 특징은?", result.getQuestion());
        assertEquals("FIFO 구조", result.getAnswer());
        verify(questionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 문제를 조회할 경우, 예외가 발생한다.")
    void getQuestionById_ThrowsException_WhenNotFound() {
        // given
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> questionService.getQuestionById(1L));
        verify(questionRepository, times(1)).findById(1L);
    }
}
