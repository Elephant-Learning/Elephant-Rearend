package me.elephantsuite.stats.service;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
import me.elephantsuite.stats.ElephantUserStatisticsRepositoryService;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ElephantUserStatisticsService {

	private final ElephantUserStatisticsRepositoryService userStatisticsRepositoryService;

	private final ElephantUserService userService;


	public Response modifyStatsOnLogin(long id) {
		ElephantUser user = userService.getUserById(id);

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User ID Invalid!")
				.addObject("userId", id)
				.build();
		}

		if (!user.isEnabled()) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User not enabled!")
				.addObject("user", user)
				.build();
		}

		user.getStatistics().incrementDaysStreak();
		user.getStatistics().resetLoginDate();

		userStatisticsRepositoryService.save(user.getStatistics());

		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Updated Login Stats!")
			.addObject("user", user)
			.build();
	}
}
