package backend.csquiz.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestionResponseDTO {
    private Long id;
    private String question;
    private String answer;
    private String difficulty;
    private List<String> optionText;
}
