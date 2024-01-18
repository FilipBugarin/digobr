package hr.fer.controller;

import hr.fer.common.ApiPaths;
import hr.fer.dto.AnalyticsDto;
import hr.fer.dto.CrosswordDto;
import hr.fer.dto.StatisticsDto;
import hr.fer.dto.SubmittedPuzzleDto;
import hr.fer.entity.common.Crossword;
import hr.fer.entity.common.PuzzleDifficulty;
import hr.fer.entity.common.PuzzleTopic;
import hr.fer.entity.common.UserCrossword;
import hr.fer.repository.CrosswordRepository;
import hr.fer.repository.PuzzleDifficultyRepository;
import hr.fer.repository.PuzzleTopicRepository;
import hr.fer.repository.UserCrosswordRepository;
import hr.fer.security.UserPrincipal;
import hr.fer.services.AnalyzePuzzleService;
import hr.fer.services.StatisticsService;
import hr.fer.services.SubmitPuzzleService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class PuzzleController {

    private final SubmitPuzzleService submitPuzzleService;
    private final StatisticsService statisticsService;

    private final PuzzleDifficultyRepository puzzleDifficultyRepository;
    private final PuzzleTopicRepository puzzleTopicRepository;

    private final CrosswordRepository crosswordRepository;

    private final UserCrosswordRepository userCrosswordRepository;
    private final AnalyzePuzzleService analyzePuzzleService;

    public PuzzleController(SubmitPuzzleService submitPuzzleService, StatisticsService statisticsService,
            PuzzleTopicRepository puzzleTopicRepository, PuzzleDifficultyRepository puzzleDifficultyRepository,
            CrosswordRepository crosswordRepository, UserCrosswordRepository userCrosswordRepository, AnalyzePuzzleService analyzePuzzleService) {
        this.submitPuzzleService = submitPuzzleService;
        this.statisticsService = statisticsService;
        this.puzzleDifficultyRepository = puzzleDifficultyRepository;
        this.puzzleTopicRepository = puzzleTopicRepository;
        this.crosswordRepository = crosswordRepository;
        this.userCrosswordRepository = userCrosswordRepository;
        this.analyzePuzzleService = analyzePuzzleService;
    }

    @PostMapping(ApiPaths.SUBMIT_PUZZLE)
    public AnalyticsDto submitPuzzle(@RequestBody SubmittedPuzzleDto submittedPuzzle) {
        submittedPuzzle.setUserId(getUser().getId());
        submitPuzzleService.submitPuzzle(submittedPuzzle);
        submitPuzzleService.updateUserStatistics(submittedPuzzle);
        return analyzePuzzleService.analyzePuzzle(submittedPuzzle);
    }

    @GetMapping(ApiPaths.GET_STATISTICS)
    public StatisticsDto getUserStatistics() {
        StatisticsDto statistics = statisticsService.getUserStatistics(getUser().getId());
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
    public List<CrosswordDto> getPreloadedPuzzleList() {
        List<Crossword> crosswords = crosswordRepository.findAll();
        List<CrosswordDto> crosswordDtos = new ArrayList<>();

        UserPrincipal currentUser = getUser();

        for (Crossword crossword : crosswords) {
            UserCrossword userCrossword = this.userCrosswordRepository.findByUserIdAndCrosswordId(currentUser.getId(),
                    crossword.getId());
            boolean liked = (userCrossword != null && userCrossword.isLiked()) ? true : false;

            crosswordDtos.add(new CrosswordDto(crossword, liked));
        }

        return crosswordDtos;
    }

    private UserPrincipal getUser() {
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
