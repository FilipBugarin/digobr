package hr.fer.controller;

import hr.fer.common.ApiPaths;
import hr.fer.dto.StatisticsDto;
import hr.fer.dto.SubmittedPuzzleDto;
import hr.fer.services.StatisticsService;
import hr.fer.services.SubmitPuzzleService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class PuzzleController {

    private final SubmitPuzzleService submitPuzzleService;
    private final StatisticsService statisticsService;

    public PuzzleController(SubmitPuzzleService submitPuzzleService, StatisticsService statisticsService) {
        this.submitPuzzleService = submitPuzzleService;
        this.statisticsService = statisticsService;
    }


    @PostMapping(ApiPaths.SUBMIT_PUZZLE)
    public void submitPuzzle(@RequestBody SubmittedPuzzleDto submittedPuzzle) {
        submitPuzzleService.submitPuzzle(submittedPuzzle);
    }

    @GetMapping(ApiPaths.GET_STATISTICS)
    public StatisticsDto getUserStatistics(@RequestParam String username) {
        StatisticsDto statistics = statisticsService.getUserStatistics(username);
        return  statistics;
    }
}
