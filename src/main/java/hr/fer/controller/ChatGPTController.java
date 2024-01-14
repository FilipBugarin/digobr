package hr.fer.controller;

import hr.fer.common.ApiPaths;
import hr.fer.common.OpenAIRequestConstants;
import hr.fer.dto.AnalyticsDto;
import hr.fer.dto.IncorrectAnswersDto;
import hr.fer.dto.PuzzleDto;
import hr.fer.dto.PuzzleTypeInfoDto;
import hr.fer.dto.openai.ChatGPTRequest;
import hr.fer.dto.openai.ChatGPTResponse;
import hr.fer.security.UserPrincipal;
import hr.fer.services.AnalyzePuzzleService;
import hr.fer.services.PuzzleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin
@RestController
public class ChatGPTController {

    @Autowired
    private RestTemplate restTemplate;

    private final PuzzleService puzzleService;
    private final AnalyzePuzzleService analyzePuzzleService;
    private final OpenAIRequestConstants openAIRequestConstants;

    public ChatGPTController(PuzzleService puzzleService, AnalyzePuzzleService analyzePuzzleService, OpenAIRequestConstants openAIRequestConstants) {
        this.puzzleService = puzzleService;
        this.analyzePuzzleService = analyzePuzzleService;
        this.openAIRequestConstants = openAIRequestConstants;
    }

    @PostMapping(ApiPaths.GENERATE_TEST_PUZZLE)
    public PuzzleDto generateTestPuzzle(@RequestBody PuzzleTypeInfoDto puzzleTypeInfo) {
        String prompt = puzzleService.createPrompt(puzzleTypeInfo);

        ChatGPTRequest request = new ChatGPTRequest(openAIRequestConstants.MODEL, prompt);
        ChatGPTResponse response = null;
        try {
            response = restTemplate.postForObject(openAIRequestConstants.API_URL, request, ChatGPTResponse.class);
        } catch (Exception e) {
            System.out.println(e);
        }

        PuzzleDto puzzle = puzzleService.createPuzzle(response, true, this.getUser(), puzzleTypeInfo);
        return puzzle;
    }

    @PostMapping(ApiPaths.GENERATE_PUZZLE)
    public PuzzleDto generatePuzzle(@RequestBody PuzzleTypeInfoDto puzzleTypeInfo) {
        String prompt = puzzleService.createPrompt(puzzleTypeInfo);

        ChatGPTRequest request = new ChatGPTRequest(openAIRequestConstants.MODEL, prompt);
        ChatGPTResponse response = null;
        try {
            response = restTemplate.postForObject(openAIRequestConstants.API_URL, request, ChatGPTResponse.class);
        } catch (Exception e) {
            System.out.println(e);
        }

        PuzzleDto puzzle = puzzleService.createPuzzle(response, false, this.getUser(), puzzleTypeInfo);
        return puzzle;
    }

    @PostMapping(ApiPaths.ANALYZE_PUZZLE)
    public AnalyticsDto analyzePuzzle(@RequestBody IncorrectAnswersDto incorrectAnswers) {
        //ako je cijela krizaljka tocno rijesena, ne treba slati ChatGPT-u na nalizu
        if(incorrectAnswers.isCorrect()) {
            return new AnalyticsDto("Sve je ispravno riješeno. Bravo!");
        }

        String prompt = analyzePuzzleService.createPrompt(incorrectAnswers);

        ChatGPTRequest request = new ChatGPTRequest(openAIRequestConstants.MODEL, prompt);
        ChatGPTResponse response = null;
        try {
            response = restTemplate.postForObject(openAIRequestConstants.API_URL, request, ChatGPTResponse.class);
        } catch (Exception e) {
            System.out.println(e);
        }

        AnalyticsDto analyticsResult = analyzePuzzleService.formatChatGPTResponse(response);

        return analyticsResult;
    }

    private UserPrincipal getUser(){
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
