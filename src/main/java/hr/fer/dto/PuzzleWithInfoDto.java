package hr.fer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PuzzleWithInfoDto {

    private PuzzleTypeInfoWithLikedDto puzzleInfo;
    private PuzzleDto puzzleDto;
}
