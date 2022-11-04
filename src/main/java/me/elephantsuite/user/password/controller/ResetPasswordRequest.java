package me.elephantsuite.user.password.controller;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class ResetPasswordRequest {

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class ResetPassword {
		private final String token;

		private final String newPassword;
	}
}
