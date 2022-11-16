package me.elephantsuite.response.exception;

import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;

public class InvalidPasswordException extends RuntimeException {

	private final String password;

	public InvalidPasswordException(String password) {
		this.password = password;
	}

	public Response toResponse() {
		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.FAILURE, "Invalid Password!")
			.addObject("incorrectPassword", password)
			.build();
	}
}
