package hr.fer.repository;

import hr.fer.entity.common.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {

    UserStatistics findByUserId(long userId);

}
