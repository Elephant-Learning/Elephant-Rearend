package me.elephantsuite.user.notification.controller;

import java.util.Objects;
import java.util.Optional;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepositoryService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
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

	public Response sendLikedDeck(NotificationRequest.LikedDeckRequest request) {

		NotificationType type = request.getType();
		String message = request.getMessage();
		ElephantUser recipient = userService.getUserById(request.getRecipientId());
		Deck deck = deckService.getDeckById(request.getDeckId());

		if (recipient == null) {
			return ResponseUtil.getInvalidUserResponse(request);
		}

		if (message == null || type == null || deck == null) {
			return ResponseUtil.getFailureResponse("Notification type, message, and deck id cannot be null/invalid!", request);
		}

		if (!recipient.isEnabled()) {
			return ResponseUtil.getFailureResponse("Recipient not enabled!", request);
		}

		if (!type.equals(NotificationType.LIKED_DECK)) {
			return ResponseUtil.getFailureResponse("Incorrect Notification Type Used! (Should Use LIKED_DECK)", request);
		}

		Notification notification = new Notification(type, message, recipient, null, deck.getId());

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

	public Response sendSharedDeck(NotificationRequest.ShareDeckRequest request) {

		NotificationType type = request.getType();
		String message = request.getMessage();
		ElephantUser recipient = userService.getUserById(request.getRecipientId());
		ElephantUser sender = userService.getUserById(request.getSenderId());
		Deck deck = deckService.getDeckById(request.getDeckId());

		if (recipient == null || sender == null) {
			return ResponseUtil.getInvalidUserResponse(request);
		}

		if (message == null || type == null || deck == null) {
			return ResponseUtil.getFailureResponse("Notification type, message, and deck id cannot be null/invalid!", request);
		}

		if (!recipient.isEnabled() || !sender.isEnabled()) {
			return ResponseUtil.getFailureResponse("Recipient or Sender not enabled!", request);
		}

		if (!type.equals(NotificationType.SHARED_DECK)) {
			return ResponseUtil.getFailureResponse("Incorrect Notification Type Used! (Should Use SHARED_DECK)", request);
		}

		Optional<Long> deckShared = recipient.getNotifications()
			.stream()
			.map(Notification::getDeckId)
			.filter(Objects::nonNull)
			.filter(aLong -> aLong.equals(deck.getId()))
			.findFirst();

		if (deckShared.isPresent()) {
			return ResponseUtil.getFailureResponse("Deck Already Shared With User!", request);
		}


		Notification notification = new Notification(type, message, recipient, request.getSenderId(), deck.getId());

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

	public Response sendFriendRequest(NotificationRequest.FriendRequest request) {

		NotificationType type = request.getType();
		String message = request.getMessage();
		ElephantUser recipient = userService.getUserById(request.getRecipientId());
		ElephantUser sender = userService.getUserById(request.getSenderId());

		if (sender == null || recipient == null) {
			return ResponseUtil.getFailureResponse("Sender or Recipient IDs are Invalid!", request);
		}

		if (message == null || type == null) {
			return ResponseUtil.getFailureResponse("Notification type or message cannot be null!", request);
		}

		if (!recipient.isEnabled() || !sender.isEnabled()) {
			return ResponseUtil.getFailureResponse("Recipient or Sender not enabled!", request);
		}

		if (!type.equals(NotificationType.FRIEND_REQUEST)) {
			return ResponseUtil.getFailureResponse("Incorrect Notification Type Used! (Should use FRIEND_REQUEST)", request);
		}

		Optional<Long> senderExists = recipient.getNotifications()
			.stream()
			.map(Notification::getSenderId)
			.filter(Objects::nonNull)
			.filter(aLong -> aLong.equals(request.getSenderId()))
			.findFirst();

		if (senderExists.isPresent()) {
			return ResponseUtil.getFailureResponse("Friend Request Notification already sent!", request);
		}

		Notification notification = new Notification(type, message, recipient, request.getSenderId(), null);

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

	public Response deleteNotification(long id) {
		Notification notification = notificationService.getById(id);

		if (notification == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Notification ID!")
				.addObject("notificationId", id)
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
