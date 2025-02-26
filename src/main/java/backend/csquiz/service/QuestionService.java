package backend.csquiz.service;

import backend.csquiz.dto.response.QuestionResponseDTO;
import backend.csquiz.entity.Question;
import backend.csquiz.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // 모든 문제 조회
    public List<QuestionResponseDTO> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 특정 문제 조회
    public QuestionResponseDTO getQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));
        return convertToDTO(question);
    }

    // 엔티티를 DTO로 변환
    private QuestionResponseDTO convertToDTO(Question question) {
        return new QuestionResponseDTO(question.getId(), question.getQuestion(), question.getAnswer(), question.getDifficulty(), question.getOptions());
    }
}
