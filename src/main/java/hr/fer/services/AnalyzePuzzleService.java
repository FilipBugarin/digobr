package hr.fer.services;

import hr.fer.common.ChatGptPrompts;
import hr.fer.dto.AnalyticsDto;
import hr.fer.dto.AnswersDto;
import hr.fer.dto.IncorrectAnswersDto;
import hr.fer.dto.PuzzleTypeInfoDto;
import hr.fer.dto.openai.ChatGPTResponse;
import org.springframework.stereotype.Service;

@Service
public class AnalyzePuzzleService {

    public String createPrompt(IncorrectAnswersDto incorrectAnswers) {

        String prompt = String.format(ChatGptPrompts.CHAT_GPT_ANALYTICS_PROMPT_1, generateVariablePart(incorrectAnswers));
        System.out.println("PROMPT:");
        System.out.println(prompt);
        return prompt;
    }

    public static String generateVariablePart(IncorrectAnswersDto incorrectAnswers) {
        String returnString = "\n";

        for(int i = 0; i<incorrectAnswers.getIncorrectAnswers().size(); i++) {
            returnString = returnString.concat((i+1)+".\n");
            returnString = returnString.concat("Question: " + incorrectAnswers.getIncorrectAnswers().get(i).desc + "\n");
            returnString = returnString.concat("Correct answer: " + incorrectAnswers.getIncorrectAnswers().get(i).word + "\n");
            returnString = returnString.concat("User's answer: " + incorrectAnswers.getIncorrectAnswers().get(i).usersAnswer + "\n\n");
        }
        return returnString;
    }

    public AnalyticsDto formatChatGPTResponse(ChatGPTResponse response) {
        AnalyticsDto analyticsResult = new AnalyticsDto();
        if(response.getChoices().isEmpty()) {
            analyticsResult.setAnalysis("Analiza rješenja trenutno nije moguća.");
        }
        else {
            analyticsResult.setAnalysis(response.getChoices().get(0).getMessage().getContent());
        }
        return analyticsResult;
    }
}
