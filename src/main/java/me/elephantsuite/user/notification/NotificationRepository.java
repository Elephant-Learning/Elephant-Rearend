package me.elephantsuite.user.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("SELECT notifications FROM ElephantUser e WHERE e.id = ?1")
	List<Notification> getByUserId(Long id);

	@Modifying
	@Transactional
	@Query("UPDATE Notification n SET n.senderId = null WHERE n.senderId = ?1")
	int deleteNotificationSenderId(long senderId);

	@Modifying
	@Transactional
	@Query("UPDATE Notification n SET n.deckId = null WHERE n.deckId = ?1")
	int deleteNotificationDeckId(long deckId);
}
