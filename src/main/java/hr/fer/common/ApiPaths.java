package hr.fer.common;

import lombok.Data;

@Data
public class ApiPaths {

    public static final String GENERATE_TEST_PUZZLE = "/api/generateTestPuzzle";
    public static final String GENERATE_PUZZLE = "/api/generatePuzzle";

    public static final String SUBMIT_PUZZLE = "/api/submitPuzzle";
    public static final String ANALYZE_PUZZLE = "/api/analyzePuzzle";

    public static final String GET_STATISTICS = "/api/statistics";

    public static final String GET_PUZZLE_DIFFICULTIES = "/api/difficulties";
    public static final String GET_PUZZLE_TOPICS = "/api/topics";
    public static final String GET_PRELOADED_PUZZLES = "/api/preloaded-puzzles";
}
