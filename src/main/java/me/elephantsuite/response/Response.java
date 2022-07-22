package me.elephantsuite.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response {

	private final ResponseStatus status;

	private final String message;

	private final Map<String, Object> context;
}
