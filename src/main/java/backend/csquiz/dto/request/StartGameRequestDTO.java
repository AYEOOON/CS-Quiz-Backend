package backend.csquiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StartGameRequestDTO {
    private String nickname;
    private int questionCount;
    private String difficulty;
}
