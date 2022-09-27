package me.elephantsuite.user.config.controller;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class UserConfigRequest {

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class ChangeBooleanValue {
		private final long userId;

		private final String term;

		private final boolean value;
	}
}
