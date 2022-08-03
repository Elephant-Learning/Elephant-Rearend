package me.elephantsuite.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response {

	private final ResponseStatus status;

	private final String message;

	// for utility/semantics
	public ResponseBuilder builder() {
		return ResponseBuilder.create();
	}
}
