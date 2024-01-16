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

    public static final String CHAT_GPT_ANALYTICS_PROMPT_1 = """
            User's answers:
            %s
                        
            Analyze each of the these answers and briefly describe the correct answer with the aim of educating the user. Write the analysis in the following format in Croatian:
                        
            Correct answer
            - information/brief description about the correct answer
                        
            Correct answer
            - information/brief description about the correct answer
                        
            Correct answer
            - information/brief description about the correct answer
            ...
                        
            Example:
            Nogomet
            - Nogomet je sport u kojemu se dvije momčadi od 11 igrača nadmeću na pravokutnom igralištu travnate površine. Cilj igre jest postizanje više pogodaka od protivničke momčadi bilo kojim dijelom tijela osim rukom. Vratar je jedini igrač kojemu je dozvoljeno igrati i braniti gol rukama, doduše samo unutar jasno označenog pravokutnika ispred vlastitih vrata. Svim igračima dopušteno je proizvoljno kretanje po terenu, iako pravilo zaleđa ograničava napadačke kretnje ovisno o položaju lopte i protivničke obrane.
                        
            Rukomet
            - Rukomet je ekipni sport s loptom, u kojem se natječu dvije momčadi sa 7 igrača na svakoj strani. Osnovni cilj igre jest loptom pogoditi označeni prostor gola. Lopta se između igrača dodaje rukama slično kao u košarci, ali s nešto manjom loptom te uz drugačija pravila vođenja lopte.
                     
            """;

public static final String GENERATE_CROSSWORD_SYSTEM_PROMPT = 
        """
        You are a skilled crossword expert specializing in creating words (answers) and clues.
        User can choose the difficulty level (easy, medium, or hard) and specify a particular topic.
        Your task is to generate a list of 30 Croatian words (answers) along with their corresponding clues for the crossword.

        Difficulty Levels:
        - Easy: Clues designed for broad appeal. The crossword should be straightforward, and around 9 out of 10 people should find most clues easy to answer.
        - Medium: Clues that offer a moderate challenge. The crossword should not be too easy, and about 6 out of 10 people should be able to answer most clues.
        - Hard: Challenging clues intended for a select few. The crossword should be difficult, and only around 3 out of 10 people should find most clues easily answerable.

        Rules:
        1. Generate 30 single-word answers.
        2. Generate 30 corresponding clues.
        2. Words (answers) should only contain letters and be exactly one word long.
        3. Words (answers) and clues must be in Croatian format, including diacritics such as č, ć, š, đ, and so on.
        4. Consider the selected difficulty level and topic while generating words (answers) and clues.
        5. Strive for specificity and uniqueness in your word selection. Challenge solvers with specialized terms, complex concepts, and clever associations.

        You will return words and clues in following format:

        *START*
        1. generated word: clue
        2. generated word: clue
        ...
        *END*
        """;


public static final String GENERATE_CROSSWORD_HUMAN_PROMPT = 
        """
        Please generate words and clues for a crossword puzzle with the following specifications:
        Topic: %s
        Difficulty: %s
        """;
}
