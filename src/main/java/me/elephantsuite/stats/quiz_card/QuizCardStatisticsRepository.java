package me.elephantsuite.stats.quiz_card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface QuizCardStatisticsRepository extends JpaRepository<QuizCardStatistics, Long> {
    @Query(value = "DELETE FROM elephant_user_statistics_quiz_card_statistics_mapping WHERE quiz_card_id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    int deleteCardMappings(long quizCardId);

    @Query(value = "DELETE FROM quiz_card_statistics WHERE quiz_card_id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    int deleteCardStats(long cardId);
}
