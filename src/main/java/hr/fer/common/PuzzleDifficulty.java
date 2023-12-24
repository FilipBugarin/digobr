package hr.fer.common;

public enum PuzzleDifficulty {
    EASY(1, "easy"),
    MEDIUM(2, "medium"),
    HARD(3, "hard");

    private final int difficulty;
    private final String description;

    PuzzleDifficulty(int difficulty, String description) {
        this.difficulty = difficulty;
        this.description = description;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getDescription() {
        return description;
    }
}
