package me.elephantsuite.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElephantUserStatisticsRepository extends JpaRepository<ElephantUserStatistics, Long> {
}
