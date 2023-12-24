package hr.fer.common;

import lombok.Data;

@Data
public class ApiPaths {

    public static final String GENERATE_TEST_PUZZLE = "/api/test/generateTestPuzzle";
    public static final String GENERATE_PUZZLE = "/api/test/generatePuzzle";

    public static final String SUBMIT_PUZZLE = "/api/test/submitPuzzle";
}
