package hr.fer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hr.fer.common.PuzzleDifficulty;
import hr.fer.common.PuzzleTopic;

import java.util.List;

public class SubmittedPuzzleDto {

    @JsonProperty("correct")
    private boolean correct;

    @JsonProperty("difficulty")
    private PuzzleDifficulty difficulty;
    @JsonProperty("topic")
    private PuzzleTopic topic;

    @JsonProperty("incorrectAnswers")
    private List<Word> incorrectAnswers;

    @Override
    public String toString() {
        return "SubmittedPuzzleDto{" +
                "correct=" + correct +
                ", difficulty=" + difficulty +
                ", topic=" + topic +
                ", incorrectAnswers=" + incorrectAnswers +
                '}';
    }
}
