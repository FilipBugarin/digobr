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
}
