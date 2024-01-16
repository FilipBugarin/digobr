package hr.fer.services;

import hr.fer.common.ChatGptPrompts;
import hr.fer.dto.PuzzleDto;
import hr.fer.dto.PuzzleTypeInfoDto;
import hr.fer.dto.Word;
import hr.fer.dto.openai.ChatGPTResponse;
import hr.fer.entity.auth.User;
import hr.fer.entity.common.Crossword;
import hr.fer.entity.common.PuzzleDifficulty;
import hr.fer.entity.common.PuzzleTopic;
import hr.fer.entity.common.UserCrossword;
import hr.fer.repository.CrosswordRepository;
import hr.fer.repository.PuzzleDifficultyRepository;
import hr.fer.repository.PuzzleTopicRepository;
import hr.fer.repository.UserCrosswordRepository;
import hr.fer.repository.UserRepository;
import hr.fer.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PuzzleService {

    @Autowired
    private CrosswordRepository crosswordRepository;

    @Autowired
    private UserCrosswordRepository userCrosswordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PuzzleDifficultyRepository puzzleDifficultyRepository;

    @Autowired
    private PuzzleTopicRepository puzzleTopicRepository;

    public static final int GRID_SIZE = 100;
    public static String[][] puzzle = new String[GRID_SIZE][GRID_SIZE];
    //1=horizontal 2=vertical
    public static int direction = 0;

    public static List<PuzzleDto> allPuzzles = new ArrayList<PuzzleDto>();
    public static List<int[]> borderIndexList = new ArrayList<>();

    public static int mL;
    public static int mT;
    public static int bestPuzzleIndex;
    public static Map<String, String> generatedWordsAndClues = new LinkedHashMap<>();

    public String createPrompt(PuzzleTopic puzzleTopic, PuzzleDifficulty puzzleDifficulty, String promptTemplate) {

        String prompt = String.format(promptTemplate,
                puzzleTopic.getTopicName(), puzzleDifficulty.getDescription());
        System.out.println("PROMPT:");
        System.out.println(prompt);
        return prompt;
    }

    public Long createPuzzle(ChatGPTResponse response, boolean testPuzzle, UserPrincipal user, PuzzleTopic puzzleTopic, PuzzleDifficulty puzzleDifficulty) {
        allPuzzles.clear();
        borderIndexList.clear();
        generatedWordsAndClues.clear();

        initializeEmptyPuzzle();

        List<String> wordList = new ArrayList<>();
        List<String[][]> listOfPuzzles = new ArrayList<>();
        List<String> questions = getQuestions(response);
        List<String> answers = getAnswers(response);

        return extractPuzzleDataFromResponse(response, user, puzzleTopic, puzzleDifficulty);

    /*
        for (int i = 0; i < 20; i++) {
            initializeEmptyPuzzle();
            wordList.clear();
            if (testPuzzle) {
                generateTestWords(wordList);
            } else {
                generateWords(wordList);
                if (wordList.size() < 10) {
                    wordList.clear();
                    generateTestWords(wordList);
                }
            }
            Collections.shuffle(wordList);
            listOfPuzzles.add(generatePuzzle(wordList));
        }

        String[][] bestPuzzle = evaluatePuzzles(listOfPuzzles);
        printPuzzle(bestPuzzle);

        mL = borderIndexList.get(bestPuzzleIndex)[0];
        mT = borderIndexList.get(bestPuzzleIndex)[1];

        formatPuzzle(bestPuzzleIndex);
        return allPuzzles.get(bestPuzzleIndex);*/
    }

    public PuzzleDto createPuzzleWithId(Long crosswordId) {
        allPuzzles.clear();
        borderIndexList.clear();
        generatedWordsAndClues.clear();

        initializeEmptyPuzzle();

        List<String> wordList = new ArrayList<>();
        List<String[][]> listOfPuzzles = new ArrayList<>();
        Optional<Crossword> crossword = crosswordRepository.findById(crosswordId);
        if (!crossword.isPresent()) {
            throw new RuntimeException("No crossword present with that id");
        }
        generatedWordsAndClues = extractWordsAndClues(crossword.get().getCrosswordDefinition());

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = userRepository.findByUsername(user.getUsername());

        UserCrossword uc = userCrosswordRepository.findByUserIdAndCrosswordId(u.getId(), crosswordId);

        if (uc == null) {
            uc = UserCrossword.builder().user(u).crossword(crossword.get()).build();
            userCrosswordRepository.save(uc);
        }

        for (int i = 0; i < 20; i++) {
            initializeEmptyPuzzle();
            wordList.clear();
            generateWords(wordList);
            if (wordList.size() < 10) {
                wordList.clear();
                generateTestWords(wordList);
            }
            Collections.shuffle(wordList);
            listOfPuzzles.add(generatePuzzle(wordList));
        }

        String[][] bestPuzzle = evaluatePuzzles(listOfPuzzles);
        printPuzzle(bestPuzzle);

        mL = borderIndexList.get(bestPuzzleIndex)[0];
        mT = borderIndexList.get(bestPuzzleIndex)[1];

        formatPuzzle(bestPuzzleIndex);
        return allPuzzles.get(bestPuzzleIndex);
    }

    public void likePuzzleById(Long crosswordId, Long userId) {
        UserCrossword uc = userCrosswordRepository.findByUserIdAndCrosswordId(userId, crosswordId);
        Crossword c = crosswordRepository.getById(crosswordId);

        if (uc.isLiked()) {
            uc.setLiked(false);
            c.setLikes(c.getLikes() - 1);
        } else {
            uc.setLiked(true);
            c.setLikes(c.getLikes() + 1);
        }
        userCrosswordRepository.save(uc);

        crosswordRepository.save(c);
    }

    private static void initializeEmptyPuzzle() {
        //inicijalizacija praznog polja
        for (String[] strings : puzzle) {
            Arrays.fill(strings, " ");
        }
    }

    public PuzzleDifficulty getPuzzleDifficulty(long id) {
        return this.puzzleDifficultyRepository.getById(id);
    }

    public PuzzleTopic getPuzzleTopic(long id) {
        return this.puzzleTopicRepository.getById(id);
    }

    private static List<String> getQuestions(ChatGPTResponse response) {
        //TODO: izvaditi pitanja iz response
        return new ArrayList<>();
    }

    private static List<String> getAnswers(ChatGPTResponse response) {
        //TODO: izvaditi odgovore iz response
        return new ArrayList<>();
    }

    private static void generateTestWords(List<String> wordList) {

        //testni skup rijeci 1
        wordList.add("Banana".toUpperCase());
        wordList.add("Suncokret".toUpperCase());
        wordList.add("Avion".toUpperCase());
        wordList.add("Knjiga".toUpperCase());
        wordList.add("Plaža".toUpperCase());
        wordList.add("Robot".toUpperCase());
        wordList.add("Planina".toUpperCase());
        wordList.add("Oblak".toUpperCase());
        wordList.add("Kava".toUpperCase());
        wordList.add("Slon".toUpperCase());
        wordList.add("More".toUpperCase());
        wordList.add("Laptop".toUpperCase());
        wordList.add("Voće".toUpperCase());
        wordList.add("Gitara".toUpperCase());
        wordList.add("Proljeće".toUpperCase());
        wordList.add("Klavir".toUpperCase());
        wordList.add("Snijeg".toUpperCase());
        wordList.add("Jagoda".toUpperCase());
        wordList.add("Balon".toUpperCase());
        wordList.add("Prijatelj".toUpperCase());

    }

    private static void generateWords(List<String> wordList) {

        int count = 0;
        for (Map.Entry<String, String> entry : generatedWordsAndClues.entrySet()) {
            wordList.add(entry.getKey().toUpperCase());
            count++;
            if (count >= 20) break;
        }
    }


    private Long extractPuzzleDataFromResponse(ChatGPTResponse response, UserPrincipal user, PuzzleTopic puzzleTopic, PuzzleDifficulty puzzleDifficulty) {

        String responseData = null;
        Map<String, String> wordsAndClues;

        boolean responseOK = checkResponse(response);
        try {
            if (responseOK) {
                responseData = extractData(response);
                System.out.println("RESPONSE:");
                System.out.println(responseData);
            }
            wordsAndClues = extractWordsAndClues(responseData);
        } catch (Exception e) {
            throw new RuntimeException("Dogodila se greška kod generiranja pojmova.");
        }

        User u = userRepository.findByUsername(user.getUsername());

        Crossword c = Crossword.builder().crosswordDefinition(responseData).topic(puzzleTopic).difficulty(puzzleDifficulty).build();
        crosswordRepository.save(c);

        UserCrossword uc = UserCrossword.builder().user(u).crossword(c).build();
        userCrosswordRepository.save(uc);

        generatedWordsAndClues = wordsAndClues;

        return c.getId();

    }

    private static String[][] generatePuzzle(List<String> wordList) {
        int maxWords = wordList.size();
        String word = wordList.get(0);

        placeHorizontal(GRID_SIZE / 2, GRID_SIZE / 2, word);
        int count = 1;

        wordList.remove(word);

        int numberOfItterations = 0;

        PuzzleDto p = new PuzzleDto();
        List<Word> wordsInPuzzle = new ArrayList<>();
        p.puzzle = wordsInPuzzle;

        NEXT:
        while (count < maxWords && wordList.size() > 0) {
            word = wordList.get(0);

            Word w = new Word();
            w.word = word;

            char[] listaSlova = word.toCharArray();
            for (int c = 0; c < listaSlova.length; c++) {
                for (int i = 0; i < GRID_SIZE; i++) {
                    for (int j = 0; j < GRID_SIZE; j++) {
                        if (puzzle[i][j].equals(String.valueOf(listaSlova[c]))) {
                            boolean canPLace = canPlace(word, i, j, c);
                            if (canPLace) {
                                if (direction == 1) {
                                    placeHorizontal(i, j - c, word);
                                    w.startPosition = new int[]{i, j - c};
                                    w.endPosition = new int[]{i, (j - c) + word.length()};
                                    w.vertical = false;
                                    w.desc = generatedWordsAndClues.get(word);
                                }
                                if (direction == 2) {
                                    placeVertical(j, i - c, word);
                                    w.startPosition = new int[]{i - c, j};
                                    w.endPosition = new int[]{i + word.length(), j - c};
                                    w.vertical = true;
                                    w.desc = generatedWordsAndClues.get(word);
                                }
                                count++;
                                wordList.remove(word);
                                wordsInPuzzle.add(w);
                                //resetiraj brojac iteracija za sljedecu rijec
                                numberOfItterations = 0;
                                continue NEXT;
                            }
                        }
                    }
                }

            }
            wordList.remove(word);
            wordList.add(word);
            numberOfItterations++;

            //ako nakon vise iteracija ne mogu posloziti rijec u krizaljku (znaci da nema zajednickih slova) - izbaci ju iz liste
            //vjerojatno bi bilo dovoljno provjeriti samo 2 iteracije (numberOfItterations==2)
            if (numberOfItterations == 5) {
                wordList.remove(word);
            }
        }

        String[][] generatedPuzzle = cutOut();
        borderIndexList.add(new int[]{mL, mT});
        allPuzzles.add(p);
        return generatedPuzzle;
    }

    //iz matrice 100*100 vraca neprazni dio matrice (samo ona polja koja sadrze pojmove)
    private static String[][] cutOut() {
        int minTop = 100;
        int minLeft = 100;
        int maxBottom = 0;
        int maxRight = 0;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (!puzzle[i][j].equals(" ") && j < minLeft) {
                    minLeft = j;
                    mL = minLeft;
                }
                if (!puzzle[i][j].equals(" ") && j > maxRight) {
                    maxRight = j;
                }
                if (!puzzle[i][j].equals(" ") && i < minTop) {
                    minTop = i;
                    mT = minTop;
                }
                if (!puzzle[i][j].equals(" ") && i > maxBottom) {
                    maxBottom = i;
                }
            }

        }
        int newRows = maxBottom - minTop + 1;
        int newColumns = maxRight - minLeft + 1;
        String[][] newPuzzle = new String[newRows][newColumns];

        for (int i = 0; i < maxBottom - minTop + 1; i++) {
            for (int j = 0; j < maxRight - minLeft + 1; j++) {
                newPuzzle[i][j] = puzzle[minTop + i][minLeft + j];
            }
        }

        return newPuzzle;
    }

    //ocjenjuje "kvalitetu" krizaljke i vraca najbolju (ocjenjuje se gustoca i "pravilna" dimenzija krizaljke)
    public static String[][] evaluatePuzzles(List<String[][]> allPuzzles) {
        double bestScore = -100.0;
        String[][] bestPuzzle = null;

        for (int i = 0; i < allPuzzles.size(); i++) {
            String[][] puzzle = allPuzzles.get(i);
            double densityScore = calculateDensity(puzzle);
            //ocjena za kvadratnu dimenziju trenutno iskljucena - cini mi se da su bolje krizaljke bez te ocjene
            double squarenessScore = /*calculateSquareness(puzzle);*/ 0;

            //bitnije je da krizaljka bude gusca, zato tu ocjenu mnozim sa vecim faktorom
            double totalScore = (densityScore * 15.0) + (squarenessScore * 5.0);
            if (totalScore > bestScore) {
                bestScore = totalScore;
                bestPuzzle = puzzle;
                bestPuzzleIndex = i;
            }
        }
        return bestPuzzle;
    }

    public static double calculateDensity(String[][] puzzle) {
        double densityScore;
        int numOfEmptyCells = 0;
        int numOfFilledCells = 0;

        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[0].length; j++) {
                if (puzzle[i][j].equals(" ")) {
                    numOfEmptyCells++;
                } else {
                    numOfFilledCells++;
                }
            }
        }

        //TODO: provjeravaj dijeljejnje s 0 (iako je "nemoguce" da bude 0 praznih celija)

        //veci omjer popunjenih čelija u odnosu na prazne = veci broj bodova
        densityScore = (numOfFilledCells * 1.0) / (numOfEmptyCells * 1.0);
        return densityScore;
    }

    public static double calculateSquareness(String[][] puzzle) {
        //ako je razlika između redaka i stupaca 0 (tj. ako je križaljka kvadratna), dodijeli maksimalan broj bodova: 1.0
        return 1.0 / (1 + (Math.abs(puzzle.length - puzzle[0].length)));
    }

    public static boolean canPlace(String word, int row, int column, int offset) {
        if (canPlaceHorizontal(word, row, column, offset)) {
            direction = 1;
            return true;
        } else if (canPlaceVertical(word, row, column, offset)) {
            direction = 2;
            return true;
        } else {
            direction = 0;
            return false;
        }
    }

    public static boolean canPlaceHorizontal(String word, int row, int column, int offset) {
        char[] listaSlova = word.toCharArray();
        int br = 0;

        //rijec ne stane u matricu
        if (column - offset + word.length() >= GRID_SIZE) {
            return false;
        }

        if (row < 0 || column < 0) {
            return false;
        }

        //provjera nedozvoljene pozicije 1 i 2
        if (offset > 0) {
            if (!puzzle[row - 1][column - 1].isBlank()) {
                return false;
            }
            if (!puzzle[row + 1][column - 1].isBlank()) {
                return false;
            }
        }

        //provjera nedozvoljene pozicije 3 i 4
        if (offset < word.length() - 1) {
            if (!puzzle[row - 1][column + 1].isBlank()) {
                return false;
            }
            if (!puzzle[row + 1][column + 1].isBlank()) {
                return false;
            }
        }

        for (int i = column - offset; i < column - offset + word.length(); i++) {
            if (!puzzle[row][i].equals(" ") && !puzzle[row][i].equals(String.valueOf(listaSlova[br]))) {
                return false;
            }
            br++;
        }
        return true;
    }

    public static boolean canPlaceVertical(String word, int row, int column, int offset) {
        char[] listaSlova = word.toCharArray();

        int br = 0;

        //rijec ne stane u matricu
        if (row - offset + word.length() >= GRID_SIZE) {
            return false;
        }

        if (row - offset < 0 || column < 0) {
            return false;
        }

        //provjera nedozvoljene pozicije 1 i 2
        if (offset > 0) {
            if (!puzzle[row - 1][column - 1].isBlank()) {
                return false;
            }
            if (!puzzle[row - 1][column + 1].isBlank()) {
                return false;
            }
        }

        //provjera nedozvoljene pozicije 3 i 4
        if (offset < word.length() - 1) {
            if (!puzzle[row + 1][column - 1].isBlank()) {
                return false;
            }
            if (!puzzle[row + 1][column + 1].isBlank()) {
                return false;
            }
        }

        for (int i = row - offset; i < row - offset + word.length(); i++) {
            if (!puzzle[i][column].equals(" ") && !puzzle[i][column].equals(String.valueOf(listaSlova[br]))) {
                return false;
            }
            br++;
        }
        return true;
    }

    public static void placeHorizontal(int row, int startingPosition, String word) {
        char[] listaSlova = word.toCharArray();
        int br = 0;
        int wordLength = word.length();
        for (int j = startingPosition; j < startingPosition + wordLength; j++) {
            puzzle[row][j] = String.valueOf(listaSlova[br]);
            br++;
        }
    }

    public static void placeVertical(int column, int startingPosition, String word) {
        char[] listaSlova = word.toCharArray();
        int br = 0;
        int wordLength = word.length();
        for (int i = startingPosition; i < startingPosition + wordLength; i++) {
            puzzle[i][column] = String.valueOf(listaSlova[br]);
            br++;
        }
    }

    public static void printPuzzle(String[][] puzzle) {
        int rows = puzzle.length;
        int columns = puzzle[0].length;

        System.out.printf("%-5s", "*");
        for (int j = 0; j < columns; j++) {
            System.out.printf("%-5s", j);
        }
        System.out.println();

        for (int i = 0; i < 5 * (columns + 1); i++) {
            System.out.print("-");
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            System.out.printf("%-3s|%1s", i, "");
            for (int j = 0; j < columns; j++) {
                System.out.printf("%-5s", puzzle[i][j]);
            }
            System.out.println();
        }

        for (int i = 0; i < 5 * (columns + 1); i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public static void formatPuzzle(int index) {
        PuzzleDto p = allPuzzles.get(index);
        for (Word w : p.puzzle) {
            int[] newPosition = new int[]{w.startPosition[0] - mT, w.startPosition[1] - mL};
            w.startPosition = newPosition;
            if (w.vertical) {
                w.endPosition = new int[]{w.startPosition[0] + w.word.length() - 1, w.startPosition[1]};
            } else {
                w.endPosition = new int[]{w.startPosition[0], w.startPosition[1] + w.word.length() - 1};
            }
        }
    }

    public static boolean checkResponse(ChatGPTResponse response) {
        if (response.getChoices().isEmpty()) {
            return false;
        }
        //todo: provjeri koji su sve moguci neispravni odgovori od chatGPTa
        return true;
    }

    public static String extractData(ChatGPTResponse response) {
        String responseData;

        try {
            String completeResponse = response.getChoices().get(0).getMessage().getContent();
            completeResponse = completeResponse.toUpperCase();
            String puzzleData = completeResponse.substring(completeResponse.indexOf("*START*") + 7, completeResponse.indexOf("*END*"));
            //System.out.println(puzzleData);
            responseData = puzzleData;
        } catch (Exception e) {
            throw new RuntimeException("response nije u traženom formatu!" + response);
        }
        return responseData;
    }

    public static Map<String, String> extractWordsAndClues(String responseData) {
        Map<String, String> wordsAndClues = new LinkedHashMap<>();

        String regex = "(\\d+\\.\\s*([A-ZČĆŽŠĐ]+)):(.*?)\\n";

        try {
            Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(responseData);

            while (matcher.find()) {
                wordsAndClues.put(matcher.group(2), matcher.group(3).trim());
            }
        } catch (Exception e) {
            throw new RuntimeException("Dogodila se greška kod formiranja rijeci za krizaljku.");
        }

        return wordsAndClues;
    }


}
