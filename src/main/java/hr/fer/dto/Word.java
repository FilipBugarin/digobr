package hr.fer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Word {
    public String word;
    public String desc;
    public int[] startPosition;
    public int[] endPosition;

    public boolean vertical;

    public Word(String word, String desc, int[] startPosition, int[] endPosition) {
        this.word = word;
        this.desc = desc;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }
}
