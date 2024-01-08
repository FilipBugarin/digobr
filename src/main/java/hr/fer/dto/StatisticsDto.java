package hr.fer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsDto {

    private int totalSolved;
    private int totalCorrect;
    private int totalIncorrect;
    private String totalPercentage;

    //todo: možda dodat statistiku za svaku kategorije posebno?? npr:
    /*private int generalKnowledgeSolved;
    private int generalKnowledgeCorrect;
    private int generalKnowledgeIncorrect;
    private String generalKnowledgePercentage;*/
}
