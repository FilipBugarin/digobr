package hr.fer.repository;

import hr.fer.entity.common.PuzzleDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuzzleDifficultyRepository extends JpaRepository<PuzzleDifficulty, Long> {

    PuzzleDifficulty findByDescription(String description);

    PuzzleDifficulty findById(long id);
}
