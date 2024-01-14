package hr.fer.config;

import hr.fer.entity.common.PuzzleDifficulty;
import hr.fer.entity.common.PuzzleTopic;
import hr.fer.repository.PuzzleDifficultyRepository;
import hr.fer.repository.PuzzleTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PuzzleDataLoader implements CommandLineRunner {

    private final PuzzleTopicRepository puzzleTopicRepository;
    private final PuzzleDifficultyRepository puzzleDifficultyRepository;

    @Autowired
    public PuzzleDataLoader(PuzzleTopicRepository puzzleTopicRepository, PuzzleDifficultyRepository puzzleDifficultyRepository) {
        this.puzzleTopicRepository = puzzleTopicRepository;
        this.puzzleDifficultyRepository = puzzleDifficultyRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Insert default values if not already present
        if (puzzleTopicRepository.findByTopicName("general knowledge") == null) {
            PuzzleTopic defaultTopic = PuzzleTopic.builder()
                    .topicName("general knowledge")
                    .build();
            puzzleTopicRepository.save(defaultTopic);
        }

        if (puzzleTopicRepository.findByTopicName("sport") == null) {
            PuzzleTopic sportTopic = PuzzleTopic.builder()
                    .topicName("sport")
                    .build();
            puzzleTopicRepository.save(sportTopic);
        }

        if (puzzleTopicRepository.findByTopicName("history") == null) {
            PuzzleTopic historyTopic = PuzzleTopic.builder()
                    .topicName("history")
                    .build();
            puzzleTopicRepository.save(historyTopic);
        }

        if (puzzleDifficultyRepository.findByDescription("easy") == null) {
            PuzzleDifficulty easy = PuzzleDifficulty.builder()
                    .description("easy")
                    .build();
            puzzleDifficultyRepository.save(easy);
        }

        if (puzzleDifficultyRepository.findByDescription("medium") == null) {
            PuzzleDifficulty medium = PuzzleDifficulty.builder()
                    .description("medium")
                    .build();
            puzzleDifficultyRepository.save(medium);
        }

        if (puzzleDifficultyRepository.findByDescription("hard") == null) {
            PuzzleDifficulty hard = PuzzleDifficulty.builder()
                    .description("hard")
                    .build();
            puzzleDifficultyRepository.save(hard);
        }
    }
}