package me.elephantsuite.stats.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "statistics")
@AllArgsConstructor
public class ElephantUserStatisticsController {

	private final ElephantUserStatisticsService service;

	@PostMapping(path = "statsOnLogin")
	public Response incrementUserStatisticsOnLogin(@RequestParam("id") long userId) {
		return service.modifyStatsOnLogin(userId);
	}

	@PostMapping(path = "increaseUsageTime")
	public Response increaseUsageTime(@RequestBody ElephantUserStatisticsRequest.IncreaseUsageTime request) {
		return service.increaseUsageTime(request);
	}

	@PostMapping(path = "card/answeredWrong")
	public Response answeredWrong(@RequestBody ElephantUserStatisticsRequest.IncrementAnsweredWrong request) {
		return service.incrementAnsweredWrong(request);
	}

	@PostMapping(path = "card/answeredRight")
	public Response answeredRight(@RequestBody ElephantUserStatisticsRequest.IncrementAnsweredRight request) {
		return service.incrementAnsweredRight(request);
	}

	@PostMapping(path = "recentlyViewedDecks")
	public Response updateRecentlyViewedDecks(@RequestBody ElephantUserStatisticsRequest.UpdateRecentlyViewedDecks request) {
		return service.updateRecentlyViewedDecks(request);
	}

	@PostMapping(path = "recentlyViewedTimelines")
	public Response updateRecentlyViewedTimeline(@RequestBody ElephantUserStatisticsRequest.UpdateRecentlyViewedTimelines request) {
		return service.updateRecentlyViewedTimelines(request);
	}
}
