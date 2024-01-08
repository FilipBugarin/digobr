package hr.fer.services;

import hr.fer.dto.StatisticsDto;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    public StatisticsDto getUserStatistics(String username) {
        StatisticsDto statistics = new StatisticsDto();

        //todo: dohvati statistiku za korisnika iz baze

        //privremeni mock podaci dok ne spojimo bazu
        statistics.setTotalSolved(12);
        statistics.setTotalCorrect(9);
        statistics.setTotalIncorrect(3);
        statistics.setTotalPercentage("0.75%");

        return statistics;
    }
}
