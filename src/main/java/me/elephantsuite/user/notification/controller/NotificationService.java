package me.elephantsuite.user.notification.controller;

import java.util.Objects;
import java.util.Optional;


import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.answers.ElephantAnswer;
import me.elephantsuite.answers.ElephantAnswerRepositoryService;
import me.elephantsuite.answers.comment.Comment;
import me.elephantsuite.answers.comment.CommentRepository;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepositoryService;
import me.elephantsuite.email.EmailService;
import me.elephantsuite.registration.RegistrationService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.exception.InvalidTagInputException;
import me.elephantsuite.response.exception.UserNotEnabledException;
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

	private final EmailService emailService;

	private final ElephantAnswerRepositoryService answerRepositoryService;

	private final CommentRepository commentRepository;

	public Response sendLikedDeck(NotificationRequest.LikedDeckRequest request) {

		NotificationType type = request.getType();
		String message = request.getMessage();
		ElephantUser recipient = ResponseUtil.checkUserValid(request.getRecipientId(), userService);
		Deck deck = deckService.getDeckById(request.getDeckId());

		if (message == null || type == null || deck == null) {
			return ResponseUtil.getFailureResponse("Notification type, message, and deck id cannot be null/invalid!", request);
		}

		if (RegistrationService.isInvalidName(message)) {
			throw new InvalidTagInputException(message);
		}

		if (!recipient.isEnabled()) {
			throw new UserNotEnabledException(recipient);
		}

		if (!type.equals(NotificationType.LIKED_DECK)) {
			return ResponseUtil.getFailureResponse("Incorrect Notification Type Used! (Should Use LIKED_DECK)", request);
		}

		Notification notification = new Notification(type, message, recipient, null, deck.getId(), null, null);

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
		ElephantUser recipient = ResponseUtil.checkUserValid(request.getRecipientId(), userService);
		ElephantUser sender = ResponseUtil.checkUserValid(request.getSenderId(), userService);
		Deck deck = deckService.getDeckById(request.getDeckId());

		if (message == null || type == null || deck == null) {
			return ResponseUtil.getFailureResponse("Notification type, message, and deck id cannot be null/invalid!", request);
		}

		if (RegistrationService.isInvalidName(message)) {
			throw new InvalidTagInputException(message);
		}

		if (!recipient.isEnabled() || !sender.isEnabled()) {
			throw new UserNotEnabledException(recipient, sender);
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


		Notification notification = new Notification(type, message, recipient, request.getSenderId(), deck.getId(), null, null);

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
		ElephantUser recipient = ResponseUtil.checkUserValid(request.getRecipientId(), userService);
		ElephantUser sender = ResponseUtil.checkUserValid(request.getSenderId(), userService);

		if (message == null || type == null) {
			return ResponseUtil.getFailureResponse("Notification type or message cannot be null!", request);
		}

		if (RegistrationService.isInvalidName(message)) {
			throw new InvalidTagInputException(message);
		}

		if (!recipient.isEnabled() || !sender.isEnabled()) {
			throw new UserNotEnabledException(recipient, sender);
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

		Notification notification = new Notification(type, message, recipient, request.getSenderId(), null, null, null);

		emailService.send(recipient.getEmail(), ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("friendEmailHtmlFile").replace("[NAME]", sender.getFullName()), "You have received a friend request!" ,true);

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
			throw new InvalidIdException(id, InvalidIdType.NOTIFICATION);
		}

		ElephantUser user = notification.getRecipient();

		if (user == null) {
			return ResponseUtil.getFailureResponse("Notification had no user!", id);
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

	public Response sendAnsweredAnswer(NotificationRequest.AnswerAnswerRequest request) {
		NotificationType type = request.getType();
		String message = request.getMessage();
		ElephantUser recipient = ResponseUtil.checkUserValid(request.getRecipientId(), userService);
		ElephantUser sender = ResponseUtil.checkUserValid(request.getSenderId(), userService);
		ElephantAnswer answer = ResponseUtil.checkEntityValid(request.getAnswerId(), answerRepositoryService.getRepository(), InvalidIdType.ANSWER);
		Comment comment = ResponseUtil.checkEntityValid(request.getCommentId(), commentRepository, InvalidIdType.COMMENT);

		if (message == null || type == null) {
			return ResponseUtil.getFailureResponse("Notification type or message cannot be null!", request);
		}

		if (RegistrationService.isInvalidName(message)) {
			throw new InvalidTagInputException(message);
		}

		if (!type.equals(NotificationType.ANSWER_ANSWER)) {
			return ResponseUtil.getFailureResponse("Incorrect Notification Type Used! (Should use ANSWER_ANSWER)", request);
		}

		Notification notification = new Notification(type, message, recipient, sender.getId(), null, answer.getId(), comment.getId());

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
}
