package hr.fer.controller;

import hr.fer.common.ApiPaths;
import hr.fer.dto.StatisticsDto;
import hr.fer.dto.SubmittedPuzzleDto;
import hr.fer.entity.common.Crossword;
import hr.fer.entity.common.PuzzleDifficulty;
import hr.fer.entity.common.PuzzleTopic;
import hr.fer.repository.CrosswordRepository;
import hr.fer.repository.PuzzleDifficultyRepository;
import hr.fer.repository.PuzzleTopicRepository;
import hr.fer.services.StatisticsService;
import hr.fer.services.SubmitPuzzleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class PuzzleController {

    private final SubmitPuzzleService submitPuzzleService;
    private final StatisticsService statisticsService;

    private final PuzzleDifficultyRepository puzzleDifficultyRepository;
    private final PuzzleTopicRepository puzzleTopicRepository;

    private final CrosswordRepository crosswordRepository;

    public PuzzleController(SubmitPuzzleService submitPuzzleService, StatisticsService statisticsService, PuzzleTopicRepository puzzleTopicRepository, PuzzleDifficultyRepository puzzleDifficultyRepository, CrosswordRepository crosswordRepository) {
        this.submitPuzzleService = submitPuzzleService;
        this.statisticsService = statisticsService;
        this.puzzleDifficultyRepository = puzzleDifficultyRepository;
        this.puzzleTopicRepository = puzzleTopicRepository;
        this.crosswordRepository = crosswordRepository;
    }


    @PostMapping(ApiPaths.SUBMIT_PUZZLE)
    public void submitPuzzle(@RequestBody SubmittedPuzzleDto submittedPuzzle) {
        submitPuzzleService.submitPuzzle(submittedPuzzle);
    }

    @GetMapping(ApiPaths.GET_STATISTICS)
    public StatisticsDto getUserStatistics(@RequestParam String username) {
        StatisticsDto statistics = statisticsService.getUserStatistics(username);
        return statistics;
    }

    @GetMapping(ApiPaths.GET_PUZZLE_DIFFICULTIES)
    public List<PuzzleDifficulty> getPuzzleDifficulties() {
        return puzzleDifficultyRepository.findAll();
    }

    @GetMapping(ApiPaths.GET_PUZZLE_TOPICS)
    public List<PuzzleTopic> getPuzzleTopics() {
        return puzzleTopicRepository.findAll();
    }

    @GetMapping(ApiPaths.GET_PRELOADED_PUZZLES)
    public List<Crossword> getPreloadedPuzzleList(){
        return crosswordRepository.findAll();
    }
}
