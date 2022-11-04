package me.elephantsuite.response.exception;

import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.user.ElephantUser;

public class UserNotEnabledException extends RuntimeException {

	private final ElephantUser[] user;

	public UserNotEnabledException(ElephantUser... user) {
		this.user = user;
	}

	public Response toResponse() {
		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.FAILURE, "User was not enabled!")
			.addObject("user", this.user)
			.build();
	}
}
