package me.elephantsuite.user.notification.service;

import java.util.List;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepositoryService;
import me.elephantsuite.deck.service.DeckService;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import me.elephantsuite.user.notification.Notification;
import me.elephantsuite.user.notification.NotificationRepositoryService;
import me.elephantsuite.user.notification.NotificationType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class NotificationService {

	private final ElephantUserService userService;

	private final NotificationRepositoryService notificationService;

	private final DeckRepositoryService deckService;

	public String sendLikedDeck(NotificationRequest.LikedDeckRequest request) {

		NotificationType type = request.getType();
		String message = request.getMessage();
		ElephantUser recipient = userService.getUserById(request.getRecipientId());
		Deck deck = deckService.getDeckById(request.getDeckId());

		if (recipient == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Sender ID is Invalid!")
				.addObject("request", request)
				.build();
		}

		if (message == null || type == null || deck == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Notification type, message, and deck id cannot be null/invalid!")
				.addObject("request", request)
				.build();
		}

		if (!type.equals(NotificationType.LIKED_DECK)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.SUCCESS, "Incorrect Notification Type Used! (Should Use LIKED_DECK)")
				.addObject("recipient", recipient)
				.addObject("request", request)
				.build();
		}

		Notification notification = new Notification(type, message, recipient, null, deck);

		recipient.getNotifications().add(notification);

		notification = notificationService.save(notification);
		recipient = userService.saveUser(recipient);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Sent Notification To User!")
			.addObject("user", recipient)
			.addObject("notification", notification)
			.build();
	}

	public String sendSharedDeck(NotificationRequest.ShareDeckRequest request) {

		NotificationType type = request.getType();
		String message = request.getMessage();
		ElephantUser recipient = userService.getUserById(request.getRecipientId());
		ElephantUser sender = userService.getUserById(request.getSenderId());
		Deck deck = deckService.getDeckById(request.getDeckId());

		if (recipient == null || sender == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Sender or Recipient IDs are Invalid!")
				.addObject("request", request)
				.build();
		}

		if (message == null || type == null || deck == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Notification type, message, and deck id cannot be null/invalid!")
				.addObject("request", request)
				.build();
		}

		if (!type.equals(NotificationType.LIKED_DECK)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.SUCCESS, "Incorrect Notification Type Used! (Should Use LIKED_DECK)")
				.addObject("recipient", recipient)
				.addObject("sender", sender)
				.addObject("request", request)
				.build();
		}

		Notification notification = new Notification(type, message, recipient, sender, deck);

		recipient.getNotifications().add(notification);

		notification = notificationService.save(notification);
		recipient = userService.saveUser(recipient);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Sent Notification To User!")
			.addObject("recipient", recipient)
			.addObject("notification", notification)
			.build();
	}

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
				.addResponse(ResponseStatus.SUCCESS, "Incorrect Notification Type Used! (Should use FRIEND_REQUEST)")
				.addObject("recipient", recipient)
				.addObject("sender", sender)
				.addObject("request", request)
				.build();
		}

		Notification notification = new Notification(type, message, recipient, sender, null);

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
