package hr.fer.dto;

import hr.fer.entity.common.PuzzleDifficulty;
import hr.fer.entity.common.PuzzleTopic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SuggestedCrossword {

    private PuzzleTopic puzzleTopic;

    private PuzzleDifficulty puzzleDifficulty;

}
