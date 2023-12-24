package hr.fer.services;

import hr.fer.dto.SubmittedPuzzleDto;
import org.springframework.stereotype.Service;

@Service
public class SubmitPuzzleService {

    public void submitPuzzle(SubmittedPuzzleDto submittedPuzzle) {
        System.out.println(submittedPuzzle);
        //TODO: spremi u bazu za kasniju analizu/statistiku ili napravi nesto s tom rijesenom krizaljkom
    }
}
