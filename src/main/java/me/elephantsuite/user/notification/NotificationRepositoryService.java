package me.elephantsuite.user.notification;

import java.util.List;

import javax.annotation.Nullable;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationRepositoryService {

	private final NotificationRepository notificationRepository;

	private final ElephantUserRepository elephantUserRepository;



	public List<Notification> getAllNotifications() {
		return notificationRepository.findAll();
	}

	public Notification addNotification(Notification notification) {
		ElephantUser user = elephantUserRepository.findById(notification.getRecipient().getId()).orElse(null);

		if (user == null) {
			return null;
		}

		notification.setRecipient(user);
		return notificationRepository.save(notification);
	}

	public List<Notification> getNotificationsByUser(ElephantUser user) {
		return notificationRepository.getByUserId(user.getId());
	}


	public void deleteNotification(Notification notification) {
		notificationRepository.delete(notification);
	}


	public Notification getById(long id) {
		try {
			return notificationRepository.getReferenceById(id);
		} catch (EntityNotFoundException e) {
			return null;
		} catch (Exception e) {
			ElephantBackendApplication.LOGGER.error("Error while getting notification by ID!", e);
			e.printStackTrace();
			return null;
		}
	}

	public Notification save(Notification notification) {
		return notificationRepository.save(notification);
	}
}
