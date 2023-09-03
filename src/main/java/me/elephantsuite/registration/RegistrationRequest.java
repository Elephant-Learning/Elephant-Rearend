package me.elephantsuite.registration;

import java.util.List;

import me.elephantsuite.deck.Deck;
import me.elephantsuite.user.ElephantUserType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.elephantsuite.user.notification.Notification;


//format for a rq
public class RegistrationRequest {
	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class CreateAccount {
		private final String firstName;

		private final String lastName;

		private final String password;

		private final String email;

		private final ElephantUserType type;

		private final Integer pfpId;

		private final Integer countryCode;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class SetAccountDetails {
		private long userId;

		private final String firstName;

		private final String lastName;

		private final Integer phoneNumber;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class DeleteAccount {
		private final long id;

		private final String password;
	}
}
