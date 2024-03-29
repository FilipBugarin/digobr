package hr.fer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SubmittedPuzzleDto {

    private Long userId;
    private Long crosswordId;
    private List<AnswersDto> incorrectAnswers;
    private List<AnswersDto> correctAnswers;
}
