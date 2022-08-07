package me.elephantsuite.stats.controller;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class ElephantUserStatisticsRequest {

	@Getter
	@AllArgsConstructor
	@ToString
	@EqualsAndHashCode
	public static class IncreaseUsageTime {

		private final long userId;

		private final double usageTime;
	}

	@Getter
	@AllArgsConstructor
	@ToString
	@EqualsAndHashCode
	public static class UpdateRecentlyViewedDecks {

		private final long userId;

		private final long deckId;
	}


	@Getter
	@AllArgsConstructor
	@ToString
	@EqualsAndHashCode
	public static class IncrementAnsweredWrong {

		private final long userId;

		private final long cardId;
	}

	@Getter
	@AllArgsConstructor
	@ToString
	@EqualsAndHashCode
	public static class IncrementAnsweredRight {

		private final long userId;

		private final long cardId;
	}
}
