package me.elephantsuite.user.notification.service;

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
}
