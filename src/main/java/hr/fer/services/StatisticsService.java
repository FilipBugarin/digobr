package hr.fer.services;

import hr.fer.dto.StatisticsDto;
import hr.fer.entity.common.UserStatistics;
import hr.fer.repository.UserStatisticsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatisticsService {

    private final UserStatisticsRepository userStatisticsRepository;

    public StatisticsService(UserStatisticsRepository userStatisticsRepository) {
        this.userStatisticsRepository = userStatisticsRepository;
    }

    public StatisticsDto getUserStatistics(long userId) {
        Optional<UserStatistics> userStatisticsOptional = Optional.ofNullable(userStatisticsRepository.findByUserId(userId));

        if (userStatisticsOptional.isPresent()) {
            UserStatistics us = userStatisticsOptional.get();
            return new StatisticsDto().builder()
                    .totalSolved(us.getTotalSolved())
                    .totalCorrect(us.getTotalCorrect())
                    .totalIncorrect(us.getTotalIncorrect())
                    .totalPercentage(calculatePercentage(us))
                    .build();
        } else {
            UserStatistics us = new UserStatistics().builder().userId(userId).build();
            userStatisticsRepository.save(us);

            return new StatisticsDto().builder()
                    .totalSolved(us.getTotalSolved())
                    .totalCorrect(us.getTotalCorrect())
                    .totalIncorrect(us.getTotalIncorrect())
                    .totalPercentage(calculatePercentage(us))
                    .build();
        }
    }

    private String calculatePercentage(UserStatistics us) {
        if(us.getTotalSolved() == 0) {
            return "-";
        } else {
            double p = (double) us.getTotalCorrect() / us.getTotalSolved() * 100;
            return String.format("%.2f%%", p);
        }
    }
}
