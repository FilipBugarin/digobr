package hr.fer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PuzzleTypeInfoWithLikedDto {
    @JsonProperty("difficultyId")
    private long difficultyId;
    @JsonProperty("topicId")
    private long topicId;
    @JsonProperty("isLiked")
    private boolean isLiked;
}
