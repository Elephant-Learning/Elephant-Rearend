package me.elephantsuite.user.notification;

import javax.annotation.Nullable;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {

	private final NotificationRepository repository;

	private Notification getById(long id) {
		return repository.getReferenceById(id);
	}
}
