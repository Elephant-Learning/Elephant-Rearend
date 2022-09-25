package me.elephantsuite.response.api;

import lombok.Getter;
import me.elephantsuite.response.util.ResponseStatus;

@Getter
public class Response {

	private final ResponseStatus status;

	private final String message;

	Response(ResponseStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	// for utility/semantics
	public ResponseBuilder builder() {
		return ResponseBuilder.create();
	}
}
