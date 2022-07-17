package me.elephantsuite.user.notification.service;

import java.util.List;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import me.elephantsuite.user.notification.Notification;
import me.elephantsuite.user.notification.NotificationRepositoryService;
import me.elephantsuite.user.notification.NotificationType;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {

	private final ElephantUserService userService;

	private final NotificationRepositoryService notificationService;

	public String sendFriendRequest(NotificationRequest.FriendRequest request) {

		NotificationType type = request.getType();
		String message = request.getMessage();
		ElephantUser recipient = userService.getUserById(request.getRecipientId());
		ElephantUser sender = userService.getUserById(request.getSenderId());

		if (sender == null || recipient == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Sender or Recipient IDs are Invalid!")
				.addObject("request", request)
				.build();
		}

		if (message == null || type == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Notification type or message cannot be null!")
				.addObject("request", request)
				.build();
		}

		if (!type.equals(NotificationType.FRIEND_REQUEST)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.SUCCESS, "Incorrect Notification Type Used!")
				.addObject("recipient", recipient)
				.addObject("sender", sender)
				.addObject("request", request)
				.build();
		}

		Notification notification = new Notification(type, message, recipient, sender);

		recipient.getNotifications().add(notification);

		notification = notificationService.save(notification);
		recipient = userService.saveUser(recipient);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Notification sent to user!")
			.addObject("notification", notification)
			.addObject("recipient", recipient)
			.addObject("sender", sender)
			.build();

	}

	public String deleteNotification(long id) {
		Notification notification = notificationService.getById(id);

		if (notification == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Notification ID!")
				.addValue(jsonObject -> jsonObject.addProperty("notificationId", id))
				.build();
		}

		ElephantUser user = notification.getRecipient();

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Notification had no user!")
				.addObject("notification", notification)
				.build();
		}

		user.getNotifications().remove(notification);

		notificationService.deleteNotification(notification);

		userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Deleted Notification!")
			.addObject("user", user)
			.addObject("notification", notification)
			.build();
	}
}
