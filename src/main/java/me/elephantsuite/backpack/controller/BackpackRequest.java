package me.elephantsuite.backpack.controller;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class BackpackRequest {

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class AddCard {

		private final long userId;

		private final long cardId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class RemoveCard {

		private final long userId;

		private final long cardId;
	}
}
