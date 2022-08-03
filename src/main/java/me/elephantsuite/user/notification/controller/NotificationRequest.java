package me.elephantsuite.user.notification.controller;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.elephantsuite.user.notification.NotificationType;


public class NotificationRequest {

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class FriendRequest {

		private final NotificationType type;

		private final String message;

		private final long senderId;

		private final long recipientId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class LikedDeckRequest {
		private final NotificationType type;

		private final String message;

		private final long recipientId;

		private final long deckId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class ShareDeckRequest {
		private final NotificationType type;

		private final String message;

		private final long recipientId;

		private final long deckId;

		private final long senderId;
	}
}
