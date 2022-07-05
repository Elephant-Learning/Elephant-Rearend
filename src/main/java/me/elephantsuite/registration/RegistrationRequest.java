package me.elephantsuite.registration;

import me.elephantsuite.user.ElephantUserType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
//format for a rq
public class RegistrationRequest {

	private final String firstName;

	private final String lastName;

	private final String password;

	private final String email;

	private final ElephantUserType type;

	private final Integer pfpId;
}
