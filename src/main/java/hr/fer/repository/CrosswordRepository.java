package hr.fer.repository;

import hr.fer.entity.common.Crossword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrosswordRepository extends JpaRepository<Crossword, Long> {
    List<Crossword> findByTopicId(Long puzzleTopicId);
}
