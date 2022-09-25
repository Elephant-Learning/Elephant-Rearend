package me.elephantsuite.response.api;

import java.util.Map;

import lombok.Getter;
import me.elephantsuite.response.util.ResponseStatus;

@Getter
public class ContextResponse extends Response {

	private final Map<String, Object> context;

	ContextResponse(ResponseStatus status, String message, Map<String, Object> context) {
		super(status, message);
		this.context = context;
	}
}
