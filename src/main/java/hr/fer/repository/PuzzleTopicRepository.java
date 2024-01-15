package hr.fer.repository;

import hr.fer.entity.common.PuzzleTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PuzzleTopicRepository extends JpaRepository<PuzzleTopic, Long> {

    PuzzleTopic findByTopicName(String TopicName);

    @Query("SELECT MAX(t.id) FROM PuzzleTopic t")
    Long findMaxId();
}
