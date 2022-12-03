package me.elephantsuite.response.exception;

import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;

public class InvalidTagInputException extends RuntimeException {

	private final String str;

	public InvalidTagInputException(String str) {
		this.str = str;
	}

	public Response toResponse() {
		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.FAILURE, "Invalid Input Given! (You tried to input an html tag)")
			.addObject("input", str)
			.build();
	}
}
