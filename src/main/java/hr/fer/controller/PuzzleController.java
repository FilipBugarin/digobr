package hr.fer.controller;

import hr.fer.common.ApiPaths;
import hr.fer.dto.SubmittedPuzzleDto;
import hr.fer.services.SubmitPuzzleService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class PuzzleController {

    private final SubmitPuzzleService submitPuzzleService;

    public PuzzleController(SubmitPuzzleService submitPuzzleService) {
        this.submitPuzzleService = submitPuzzleService;
    }


    @PostMapping(ApiPaths.SUBMIT_PUZZLE)
    public void submitPuzzle(@RequestBody SubmittedPuzzleDto submittedPuzzle) {
        submitPuzzleService.submitPuzzle(submittedPuzzle);
    }
}
