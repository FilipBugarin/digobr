package hr.fer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import hr.fer.entity.common.Crossword;
import hr.fer.entity.common.PuzzleDifficulty;
import hr.fer.entity.common.PuzzleTopic;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrosswordDto {
    private Long id;
    private String crosswordDefinition;
    private PuzzleDifficulty difficulty;
    private PuzzleTopic topic;
    private Integer likes;
    private boolean likedByUser;

    public CrosswordDto(Crossword crossword, boolean liked) {
        this.id = crossword.getId();
        this.crosswordDefinition = crossword.getCrosswordDefinition();
        this.difficulty = crossword.getDifficulty();
        this.topic = crossword.getTopic();
        this.likes = crossword.getLikes();
        this.likedByUser = liked;
    }
}