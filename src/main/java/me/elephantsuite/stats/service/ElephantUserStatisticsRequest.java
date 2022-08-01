package me.elephantsuite.stats.service;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ElephantUserStatisticsRequest {

	@Getter
	@AllArgsConstructor
	@ToString
	@EqualsAndHashCode
	public static class IncreaseUsageTimeRequest {

		private final long userId;

		private final double usageTime;
	}
}
