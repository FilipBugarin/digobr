package hr.fer.repository;

import hr.fer.entity.common.PuzzleTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuzzleTopicRepository extends JpaRepository<PuzzleTopic, Long> {

    PuzzleTopic findByTopicName(String TopicName);

    PuzzleTopic findById(long id);
}
