package me.elephantsuite.stats.service;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import org.springframework.web.bind.annotation.PostMapping;
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
}
