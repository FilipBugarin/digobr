package hr.fer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WordClueDto {

    @JsonProperty("word")
    public String word;

    @JsonProperty("clue")
    public String clue;
}
