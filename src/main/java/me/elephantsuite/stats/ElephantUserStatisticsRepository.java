package me.elephantsuite.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ElephantUserStatisticsRepository extends JpaRepository<ElephantUserStatistics, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM elephant_user_statistics_recently_viewed_deck_ids WHERE elephant_user_statistics_recently_viewed_deck_ids.recently_viewed_deck_ids = ?1", nativeQuery = true)
    int deleteRecentlyViewedDeck(long deckId);
}
