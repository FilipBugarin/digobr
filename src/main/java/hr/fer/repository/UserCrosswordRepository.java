package hr.fer.repository;

import hr.fer.entity.common.UserCrossword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCrosswordRepository extends JpaRepository<UserCrossword, Long> {

    UserCrossword findByUserIdAndCrosswordId(Long userId, Long crosswordId);

}
