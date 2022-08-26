package me.elephantsuite.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;

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
