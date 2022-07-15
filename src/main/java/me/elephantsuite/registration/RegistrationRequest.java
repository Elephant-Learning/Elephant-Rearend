package me.elephantsuite.registration;

import java.util.List;

import me.elephantsuite.user.ElephantUserType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
//format for a rq
public final class RegistrationRequest {

	private final String firstName;

	private final String lastName;

	private final String password;

	private final String email;

	private final ElephantUserType type;

	private final Integer pfpId;

	private final List<Long> friendIds;

}
