package backend.csquiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CheckAnswerRequestDTO {
    private String gameId;
    private Long questionId;
    private String answer;
}
