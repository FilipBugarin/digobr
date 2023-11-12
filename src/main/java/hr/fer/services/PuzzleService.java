package hr.fer.services;

import hr.fer.common.PuzzleDifficulty;
import hr.fer.dto.PuzzleTypeInfoDto;
import org.springframework.stereotype.Service;

@Service
public class PuzzleService {

    public String createPrompt(PuzzleTypeInfoDto puzzleTypeInfo) {
        //TODO: formiraj prompt koristeci podatke iz PuzzleTypeInfoDto

        //testni prompt
        String prompt;
        if(puzzleTypeInfo.getDifficulty() == PuzzleDifficulty.EASY) {
            prompt = "When was Leonardo da Vinci born?";
        } else {
            prompt = "When did Leonardo da Vinci die?";
        }
        return prompt;
    }
}
