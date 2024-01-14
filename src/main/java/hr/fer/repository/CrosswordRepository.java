package hr.fer.repository;

import hr.fer.entity.common.Crossword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrosswordRepository extends JpaRepository<Crossword, Long> {
}
