package hr.fer.services;

import hr.fer.dto.SubmittedPuzzleDto;
import hr.fer.entity.common.UserCrossword;
import hr.fer.repository.UserCrosswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmitPuzzleService {

    @Autowired
    UserCrosswordRepository userCrosswordRepository;

    public void submitPuzzle(SubmittedPuzzleDto submittedPuzzle) {
        UserCrossword uc = userCrosswordRepository.findByUserIdAndCrosswordId(submittedPuzzle.getUserId(), submittedPuzzle.getCrosswordId());
        uc.setCorrectAnswers(submittedPuzzle.getCorrectAnswers().size());
        uc.setIncorectAnswers(submittedPuzzle.getIncorrectAnswers().size());
    }
}
