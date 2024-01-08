package hr.fer.services;

import hr.fer.dto.SubmittedPuzzleDto;
import org.springframework.stereotype.Service;

@Service
public class SubmitPuzzleService {

    public void submitPuzzle(SubmittedPuzzleDto submittedPuzzle) {
        System.out.println(submittedPuzzle.toString());
        //TODO: spremi u bazu za kasniju analizu/statistiku ili napravi nesto s tom rijesenom krizaljkom
        //dovoljno je povecati broj rijesenih krizaljki i broj tocnih/netocnih krizaljki (pogledaj StatisticsDto, to ce se dohvacat iz baze)
    }
}
