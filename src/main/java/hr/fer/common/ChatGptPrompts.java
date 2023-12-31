package hr.fer.common;

public class ChatGptPrompts {

    public static final String CHAT_GPT_PROMPT_1 = """
            I'm creating a crossword puzzle on a specific topic with a specific difficulty.
                                        
            There are 3 difficulty levels: easy, medium, hard.
                         
            Easy: Clues which almost everyone should be able to answer. The crossword should be easy to solve. 9/10 people should be able to answer most clues
            Medium: Clues which some people should be able to answer. The crossword should not be too easy to solve. 6/10 people should be able to answer most clues
            Hard: Clues which only a few people should be able to answer without any problem. The crossword should be hard to solve. 3/10 people should be able to answer most clues
                                        
            Generate 30 words and 30 corresponding clues on topic "%s" for a crossword puzzle, difficulty: %s. Words/answers to clues should only contain letters and be one word long.
            Write words and clues in Croatian in format:
                                        
            *START*
            1. generated word: clue
            2. generated word: clue
            ...
            *END*
            """;
}
