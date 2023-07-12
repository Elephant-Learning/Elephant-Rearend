package me.elephantsuite.timeline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineRepository extends JpaRepository<Timeline, Long> {


	@Modifying
	@Query(value = "DELETE FROM elephant_user_liked_timeline_ids WHERE liked_timeline_ids = ?1", nativeQuery = true)
	int deleteLikedTimelineIds(long id);

	@Modifying
	@Query(value = "DELETE FROM elephant_user_shared_timeline_ids WHERE shared_timeline_ids = ?1", nativeQuery = true)
	int deleteSharedTimelineIds(long id);

	@Modifying
	@Query(value = "DELETE FROM elephant_user_statistics_recently_viewed_timeline_ids WHERE recently_viewed_timeline_ids = ?1", nativeQuery = true)
	int deleteRecentlyViewedTimelineIds(long id);

	@Modifying
	@Query(value = "DELETE FROM folder_timeline_ids WHERE timeline_ids = ?1", nativeQuery = true)
	int deleteFolderTimelineIds(long id);

	@Modifying
	@Query("DELETE FROM Timeline t WHERE t.id = ?1")
	int deleteById(long id);
}
