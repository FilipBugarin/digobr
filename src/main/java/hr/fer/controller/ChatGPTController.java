package hr.fer.controller;

import hr.fer.common.ApiPaths;
import hr.fer.common.ChatGptPrompts;
import hr.fer.common.OpenAIRequestConstants;
import hr.fer.dto.PuzzleDto;
import hr.fer.dto.PuzzleTypeInfoDto;
import hr.fer.dto.openai.ChatGPTRequest;
import hr.fer.dto.openai.ChatGPTResponse;
import hr.fer.entity.common.PuzzleDifficulty;
import hr.fer.entity.common.PuzzleTopic;
import hr.fer.security.UserPrincipal;
import hr.fer.services.PuzzleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@CrossOrigin
@RestController
public class ChatGPTController {

    @Autowired
    private RestTemplate restTemplate;

    private final PuzzleService puzzleService;
    private final OpenAIRequestConstants openAIRequestConstants;

    public ChatGPTController(PuzzleService puzzleService, OpenAIRequestConstants openAIRequestConstants) {
        this.puzzleService = puzzleService;
        this.openAIRequestConstants = openAIRequestConstants;
    }

    @PostMapping(ApiPaths.GENERATE_TEST_PUZZLE)
    public Long generateTestPuzzle(@RequestBody PuzzleTypeInfoDto puzzleTypeInfo) {
        PuzzleTopic puzzleTopic = puzzleService.getPuzzleTopic(puzzleTypeInfo.getTopicId());
        PuzzleDifficulty puzzleDifficulty = puzzleService.getPuzzleDifficulty(puzzleTypeInfo.getDifficultyId());

        String prompt = puzzleService.createPrompt(puzzleTopic, puzzleDifficulty, ChatGptPrompts.CHAT_GPT_PROMPT_1);

        ChatGPTRequest request = new ChatGPTRequest(openAIRequestConstants.MODEL, prompt);
        ChatGPTResponse response = null;
        try {
            response = restTemplate.postForObject(openAIRequestConstants.API_URL, request, ChatGPTResponse.class);
        } catch (Exception e) {
            System.out.println(e);
        }

        return puzzleService.createPuzzle(response, true, this.getUser(), puzzleTopic, puzzleDifficulty);
    }

    @PostMapping(ApiPaths.GENERATE_PUZZLE)
    public Long generatePuzzle(@RequestBody PuzzleTypeInfoDto puzzleTypeInfo) {
        PuzzleTopic puzzleTopic = puzzleService.getPuzzleTopic(puzzleTypeInfo.getTopicId());
        PuzzleDifficulty puzzleDifficulty = puzzleService.getPuzzleDifficulty(puzzleTypeInfo.getDifficultyId());

        String userPrompt = puzzleService.createPrompt(puzzleTopic, puzzleDifficulty, ChatGptPrompts.GENERATE_CROSSWORD_HUMAN_PROMPT);
        String systemPrompt = ChatGptPrompts.GENERATE_CROSSWORD_SYSTEM_PROMPT;

        ChatGPTRequest request = new ChatGPTRequest(openAIRequestConstants.MODEL, userPrompt, systemPrompt);
        ChatGPTResponse response = null;
        try {
            response = restTemplate.postForObject(openAIRequestConstants.API_URL, request, ChatGPTResponse.class);
        } catch (Exception e) {
            System.out.println(e);
        }
        return puzzleService.createPuzzle(response, false, this.getUser(), puzzleTopic, puzzleDifficulty);
    }

    @GetMapping(ApiPaths.GENERATE_PUZZLE_BY_ID)
    public PuzzleDto generatePuzzle(@RequestParam Long crosswordId){
        return puzzleService.createPuzzleWithId(crosswordId);
    }

    @PostMapping(ApiPaths.LIKE_PUZZLE_BY_ID)
    public void likePuzzle(@RequestParam Long crosswordId){
        puzzleService.likePuzzleById(crosswordId, this.getUser().getId());
    }

    private UserPrincipal getUser(){
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
