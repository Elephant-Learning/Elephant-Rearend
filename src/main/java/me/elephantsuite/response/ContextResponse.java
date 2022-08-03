package me.elephantsuite.response;

import java.util.Map;

import lombok.Getter;

@Getter
public class ContextResponse extends Response {

	private final Map<String, Object> context;

	public ContextResponse(ResponseStatus status, String message, Map<String, Object> context) {
		super(status, message);
		this.context = context;
	}
}
