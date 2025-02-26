package backend.csquiz.repository;

import backend.csquiz.entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByDifficulty(String difficulty); // 난이도에 따라 문제 조회
}