package hr.fer.repository;

import hr.fer.entity.common.UserCrossword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCrosswordRepository extends JpaRepository<UserCrossword, Long> {

    UserCrossword findByUserIdAndCrosswordId(Long userId, Long crosswordId);

    List<UserCrossword> findByUserId(Long userId);
}
