package backend.csquiz.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameStartResponseDTO {
    private String gameId;
    private List<QuestionResponseDTO> gameQuestionList;
}
