package me.elephantsuite.stats.quiz_card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizCardStatisticsRepository extends JpaRepository<QuizCardStatistics, Long> {
}
