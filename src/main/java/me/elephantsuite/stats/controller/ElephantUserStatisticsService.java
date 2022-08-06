package me.elephantsuite.stats.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.deck.card.CardService;
import me.elephantsuite.response.Response;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
import me.elephantsuite.stats.ElephantUserStatisticsRepositoryService;
import me.elephantsuite.stats.card.CardStatistics;
import me.elephantsuite.stats.card.CardStatisticsService;
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

	private final CardService cardService;

	private final CardStatisticsService cardStatisticsService;

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

	public Response increaseUsageTime(ElephantUserStatisticsRequest.IncreaseUsageTime request) {
		long userId = request.getUserId();
		double usageTime = request.getUsageTime();

		ElephantUser user = userService.getUserById(userId);

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User ID Invalid!")
				.addObject("userId", userId)
				.build();
		}

		if (!user.isEnabled()) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User not enabled!")
				.addObject("user", user)
				.build();
		}

		user.getStatistics().increaseUsageTime(usageTime);

		userStatisticsRepositoryService.save(user.getStatistics());

		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Updated Usage Time!")
			.addObject("user", user)
			.build();
	}

	public Response incrementAnsweredWrong(ElephantUserStatisticsRequest.IncrementAnsweredWrong request) {
		long userId = request.getUserId();
		long cardId = request.getCardId();

		ElephantUser user = userService.getUserById(userId);
		Card card = cardService.getCardById(cardId);

		if (card == null || user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid User or Card IDs!")
				.addObject("request", request)
				.build();
		}

		if(!user.getStatistics().getCardStatistics().containsKey(card)) {
			user.getStatistics().getCardStatistics().put(card, new CardStatistics(cardId));
		}

		user.getStatistics().getCardStatistics().get(card).incrementAnsweredWrong();

		cardStatisticsService.save(user.getStatistics().getCardStatistics().get(card));
		userStatisticsRepositoryService.save(user.getStatistics());
		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Incremented Answered Wrong for Card!")
			.addObject("user", user)
			.build();
	}

	public Response incrementAnsweredRight(ElephantUserStatisticsRequest.IncrementAnsweredRight request) {
		long userId = request.getUserId();
		long cardId = request.getCardId();

		ElephantUser user = userService.getUserById(userId);
		Card card = cardService.getCardById(cardId);

		if (card == null || user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid User or Card IDs!")
				.addObject("request", request)
				.build();
		}

		if(!user.getStatistics().getCardStatistics().containsKey(card)) {
			user.getStatistics().getCardStatistics().put(card, new CardStatistics(cardId));
		}

		user.getStatistics().getCardStatistics().get(card).incrementAnsweredRight();

		cardStatisticsService.save(user.getStatistics().getCardStatistics().get(card));
		userStatisticsRepositoryService.save(user.getStatistics());
		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Incremented Answered Right for Card!")
			.addObject("user", user)
			.build();
	}
}
