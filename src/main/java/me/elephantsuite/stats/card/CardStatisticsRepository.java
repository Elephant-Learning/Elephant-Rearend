package me.elephantsuite.stats.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CardStatisticsRepository extends JpaRepository<CardStatistics, Long> {

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM elephant_user_statistics_card_statistics_mapping WHERE elephant_user_statistics_card_statistics_mapping.card_id = ?1", nativeQuery = true)
	int deleteCardStatisticsMapping(long cardId);

	@Modifying
	@Transactional
	@Query("DELETE FROM CardStatistics c WHERE c.cardId = ?1")
	int deleteCardStatistics(long cardId);
}
