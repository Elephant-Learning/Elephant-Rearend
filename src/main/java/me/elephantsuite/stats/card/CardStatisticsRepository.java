package me.elephantsuite.stats.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardStatisticsRepository extends JpaRepository<CardStatistics, Long> {
}
