package me.elephantsuite.registration;

import java.util.List;

import me.elephantsuite.user.ElephantUserType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.elephantsuite.user.notification.Notification;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
//format for a rq
public class RegistrationRequest {

	private final String firstName;

	private final String lastName;

	private final String password;

	private final String email;

	private final ElephantUserType type;

	private final Integer pfpId;

	private final List<Long> friendIds;

	private final List<Notification> notifications;
}
