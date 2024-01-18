package hr.fer.services;

import hr.fer.dto.SubmittedPuzzleDto;
import hr.fer.entity.common.UserCrossword;
import hr.fer.entity.common.UserStatistics;
import hr.fer.repository.UserCrosswordRepository;
import hr.fer.repository.UserStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubmitPuzzleService {

    @Autowired
    UserCrosswordRepository userCrosswordRepository;
    @Autowired
    UserStatisticsRepository userStatisticsRepository;

    public void submitPuzzle(SubmittedPuzzleDto submittedPuzzle) {
        UserCrossword uc = userCrosswordRepository.findByUserIdAndCrosswordId(submittedPuzzle.getUserId(), submittedPuzzle.getCrosswordId());
        uc.setCorrectAnswers(submittedPuzzle.getCorrectAnswers().size());
        uc.setIncorectAnswers(submittedPuzzle.getIncorrectAnswers().size());

        userCrosswordRepository.save(uc);
    }

    public void updateUserStatistics(SubmittedPuzzleDto submittedPuzzle) {

        Optional<UserStatistics> userStatisticsOptional = Optional.ofNullable(userStatisticsRepository.findByUserId(submittedPuzzle.getUserId()));

        if (userStatisticsOptional.isPresent()) {
            UserStatistics userStatistics = userStatisticsOptional.get();

            userStatistics.setTotalSolved(userStatistics.getTotalSolved() + 1);
            if(submittedPuzzle.getIncorrectAnswers().isEmpty()) {
                userStatistics.setTotalCorrect(userStatistics.getTotalCorrect() + 1);
            } else {
                userStatistics.setTotalIncorrect(userStatistics.getTotalIncorrect() + 1);
            }
            userStatisticsRepository.save(userStatistics);
        } else {
            boolean isCorrect = submittedPuzzle.getIncorrectAnswers().isEmpty();

            UserStatistics us = new UserStatistics().builder()
                    .totalSolved(1)
                    .totalCorrect(isCorrect ? 1 : 0)
                    .totalIncorrect(isCorrect ? 0 : 1)
                    .userId(submittedPuzzle.getUserId()).build();
            userStatisticsRepository.save(us);
        }
    }
}
