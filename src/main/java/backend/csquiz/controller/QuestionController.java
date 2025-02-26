package backend.csquiz.controller;

import backend.csquiz.dto.response.QuestionResponseDTO;
import backend.csquiz.service.QuestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // 문제 목록 조회
    @GetMapping
    public List<QuestionResponseDTO> getAllQuestions() {
        return questionService.getAllQuestions();
    }
}
