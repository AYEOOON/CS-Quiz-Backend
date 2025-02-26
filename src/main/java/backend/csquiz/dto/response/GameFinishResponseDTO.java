package backend.csquiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameFinishResponseDTO {
    private String message;
    private int finalScore;
}
