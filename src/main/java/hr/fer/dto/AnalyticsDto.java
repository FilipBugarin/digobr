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
public class AnalyticsDto {

    @JsonProperty("analysis")
    private String analysis;

    private SuggestedCrossword suggestedCrossword;

}
