package hr.fer.services;

import hr.fer.common.ChatGptPrompts;
import hr.fer.common.OpenAIRequestConstants;
import hr.fer.dto.AnalyticsDto;
import hr.fer.dto.AnswersDto;
import hr.fer.dto.SubmittedPuzzleDto;
import hr.fer.dto.SuggestedCrossword;
import hr.fer.dto.openai.ChatGPTRequest;
import hr.fer.dto.openai.ChatGPTResponse;
import hr.fer.entity.common.Crossword;
import hr.fer.entity.common.PuzzleDifficulty;
import hr.fer.entity.common.PuzzleTopic;
import hr.fer.entity.common.UserCrossword;
import hr.fer.repository.CrosswordRepository;
import hr.fer.repository.PuzzleDifficultyRepository;
import hr.fer.repository.PuzzleTopicRepository;
import hr.fer.repository.UserCrosswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Service
public class AnalyzePuzzleService {

    @Autowired
    private RestTemplate restTemplate;
    private final OpenAIRequestConstants openAIRequestConstants;
    private final UserCrosswordRepository userCrosswordRepository;
    private final CrosswordRepository crosswordRepository;
    private final PuzzleTopicRepository puzzleTopicRepository;
    private final PuzzleDifficultyRepository puzzleDifficultyRepository;

    public AnalyzePuzzleService(OpenAIRequestConstants openAIRequestConstants, CrosswordRepository crosswordRepository, PuzzleTopicRepository puzzleTopicRepository, PuzzleDifficultyRepository puzzleDifficultyRepository, UserCrosswordRepository userCrosswordRepository) {
        this.openAIRequestConstants = openAIRequestConstants;
        this.crosswordRepository = crosswordRepository;
        this.puzzleTopicRepository = puzzleTopicRepository;
        this.puzzleDifficultyRepository = puzzleDifficultyRepository;
        this.userCrosswordRepository = userCrosswordRepository;
    }

    public AnalyticsDto analyzePuzzle(SubmittedPuzzleDto submittedPuzzleDto) {
        //ako je cijela krizaljka tocno rijesena, ne treba slati ChatGPT-u na nalizu
        if (submittedPuzzleDto.getIncorrectAnswers().isEmpty()) {

            long suggestedDifficulty;
            long suggestedTopic;

            Crossword c = crosswordRepository.findById(submittedPuzzleDto.getCrosswordId()).get();
            if (c.getDifficulty().getId() == 3) {
                suggestedDifficulty = 1L;
                Long maxTopicId = puzzleTopicRepository.findMaxId();

                Random random = new Random();
                int randomNum;
                do {
                    randomNum = random.nextInt(maxTopicId.intValue());
                } while (randomNum == c.getTopic().getId().intValue());

                suggestedTopic = randomNum;
            } else {
                suggestedDifficulty = c.getDifficulty().getId() + 1;
                suggestedTopic = c.getTopic().getId();
            }

            SuggestedCrossword suggestedCrossword = SuggestedCrossword.builder().puzzleTopic(suggestedTopic).puzzleDifficulty(suggestedDifficulty).build();

            return new AnalyticsDto("Sve je ispravno riješeno. Bravo!", suggestedCrossword);
        }

        String prompt = this.createPrompt(submittedPuzzleDto.getIncorrectAnswers());

        ChatGPTRequest request = new ChatGPTRequest(openAIRequestConstants.MODEL, prompt);
        ChatGPTResponse response = null;
        try {
            response = restTemplate.postForObject(openAIRequestConstants.API_URL, request, ChatGPTResponse.class);
            System.out.println("CHATGPT ANALYTICS RESPONSE:");
            System.out.println(response);
        } catch (Exception e) {
            System.out.println(e);
        }

        AnalyticsDto analyticsResult = this.formatChatGPTResponse(response);
        analyticsResult.setSuggestedCrossword(analyzeResults(submittedPuzzleDto));

        return analyticsResult;
    }

    private SuggestedCrossword analyzeResults(SubmittedPuzzleDto submittedPuzzleDto) {

        long suggestedDifficulty;
        long suggestedTopic;

        //analysis of users previus atempts
        //=====================================

        List<UserCrossword> performanceHistory = getUserPerformanceHistory(submittedPuzzleDto.getUserId());

        if (isImprovingTrend(performanceHistory)) {
            Crossword c = crosswordRepository.findById(submittedPuzzleDto.getCrosswordId()).get();
            if (c.getDifficulty().getId() == 3) {
                suggestedDifficulty = 1L;
                Long maxTopicId = puzzleTopicRepository.findMaxId();

                Random random = new Random();
                int randomNum;
                do {
                    randomNum = random.nextInt(maxTopicId.intValue());
                } while (randomNum == c.getTopic().getId().intValue());

                suggestedTopic = randomNum;
            } else {
                suggestedDifficulty = c.getDifficulty().getId() + 1;
                suggestedTopic = c.getTopic().getId();
            }
        } else {
            return null;
        }
        //=====================================

        return SuggestedCrossword.builder().puzzleTopic(suggestedTopic).puzzleDifficulty(suggestedDifficulty).build();
    }

    public List<UserCrossword> getUserPerformanceHistory(Long userId) {
        return userCrosswordRepository.findByUserId(userId);
    }

    public boolean isImprovingTrend(List<UserCrossword> performanceHistory) {
        if (performanceHistory.size() < 2) {
            // Not enough data to determine a trend
            return false;
        }

        int consecutiveImprovements = 0;

        for (int i = 1; i < performanceHistory.size(); i++) {
            int currentCorrectAnswers = performanceHistory.get(i).getCorrectAnswers();
            int currentIncorrectAnswers = performanceHistory.get(i).getIncorectAnswers();
            int previousCorrectAnswers = performanceHistory.get(i - 1).getCorrectAnswers();
            int previousIncorrectAnswers = performanceHistory.get(i - 1).getIncorectAnswers();

            int sumCurrentAnswers = currentCorrectAnswers + currentIncorrectAnswers;
            int sumpreviousAnswers = previousCorrectAnswers + previousIncorrectAnswers;


            if(sumCurrentAnswers == 0 || sumpreviousAnswers == 0){
                consecutiveImprovements = 0;
                continue;
            }

            if(currentCorrectAnswers/sumCurrentAnswers < 0.5 || previousCorrectAnswers/sumpreviousAnswers < 0.5){
                consecutiveImprovements = 0;
                continue;
            }

            if (currentCorrectAnswers >= previousCorrectAnswers) {
                // Check if there's a consecutive improvement
                consecutiveImprovements++;
            } else {
                // Reset consecutiveImprovements if the trend breaks
                consecutiveImprovements = 0;
            }

            // You can adjust the threshold based on your requirements
            // if set to 1 then after 2 good crossword solving it will suggest harder difficulty
            if (consecutiveImprovements >= 1) {
                // Consider it as an improving trend
                return true;
            }
        }

        // No significant improving trend detected
        return false;
    }


    public String createPrompt(List<AnswersDto> incorrectAnswers) {

        String prompt = String.format(ChatGptPrompts.CHAT_GPT_ANALYTICS_PROMPT_1, generateVariablePart(incorrectAnswers));
        System.out.println("PROMPT:");
        System.out.println(prompt);
        return prompt;
    }

    public static String generateVariablePart(List<AnswersDto> incorrectAnswers) {
        String returnString = "\n";

        for (int i = 0; i < incorrectAnswers.size(); i++) {
            returnString = returnString.concat((i + 1) + ".\n");
            returnString = returnString.concat("Question: " + incorrectAnswers.get(i).desc + "\n");
            returnString = returnString.concat("Correct answer: " + incorrectAnswers.get(i).word + "\n");
            returnString = returnString.concat("User's answer: " + incorrectAnswers.get(i).usersAnswer + "\n\n");
        }
        return returnString;
    }

    public AnalyticsDto formatChatGPTResponse(ChatGPTResponse response) {
        AnalyticsDto analyticsResult = new AnalyticsDto();
        if (response.getChoices().isEmpty()) {
            analyticsResult.setAnalysis("Analiza rješenja trenutno nije moguća.");
        } else {
            System.out.println("FORMATED ANALYSIS:");
            System.out.println(response.getChoices().get(0).getMessage().getContent());
            analyticsResult.setAnalysis(response.getChoices().get(0).getMessage().getContent());
        }
        return analyticsResult;
    }
}
