package hr.fer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PuzzleDto {
    public List<Word> puzzle;

    @Override
    public String toString() {
        String s = "";
        for(Word w : puzzle) {
            s = s.concat(w.word + " : startPosition=["+ w.startPosition[0] + ", " + w.startPosition[1] + "]" + " ; endPosition=["+ w.endPosition[0] + ", " + w.endPosition[1] + "]");
            s = s.concat("\n");
        }
        return s;
    }

    public List<Word> getPuzzle() {
        return puzzle;
    }
}
