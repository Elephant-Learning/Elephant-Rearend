package me.elephantsuite.misc;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class MiscRequest {

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class SetPfpId {
		private final long userId;

		private final int pfpId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class SetCountryCode {
		private final long userId;

		private final int countryCode;
	}
}
