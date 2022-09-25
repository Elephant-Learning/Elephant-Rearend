package me.elephantsuite.stats.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.config.PropertiesHandler;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepositoryService;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.deck.card.CardService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
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
	private DeckRepositoryService deckService;

	public Response modifyStatsOnLogin(long id) {
		ElephantUser user = userService.getUserById(id);

		if (user == null) {
			return ResponseUtil.getInvalidUserResponse(id);
		}

		if (!user.isEnabled()) {
			return ResponseUtil.getFailureResponse("User not enabled!", user);
		}

		user.getElephantUserStatistics().incrementDaysStreak();
		user.getElephantUserStatistics().resetLoginDate();

		userStatisticsRepositoryService.save(user.getElephantUserStatistics());

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
			return ResponseUtil.getInvalidUserResponse(userId);
		}

		if (!user.isEnabled()) {
			return ResponseUtil.getFailureResponse("User not enabled!", user);
		}

		user.getElephantUserStatistics().increaseUsageTime(usageTime);

		userStatisticsRepositoryService.save(user.getElephantUserStatistics());

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
			return ResponseUtil.getFailureResponse("Invalid User or Card IDs!", request);
		}

		if(!user.getElephantUserStatistics().getCardStatistics().containsKey(card)) {
			user.getElephantUserStatistics().getCardStatistics().put(card, new CardStatistics(cardId));
		}

		user.getElephantUserStatistics().getCardStatistics().get(card).incrementAnsweredWrong();

		cardStatisticsService.save(user.getElephantUserStatistics().getCardStatistics().get(card));
		userStatisticsRepositoryService.save(user.getElephantUserStatistics());
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
			return ResponseUtil.getFailureResponse("Invalid User or Card IDs!", request);
		}

		if(!user.getElephantUserStatistics().getCardStatistics().containsKey(card)) {
			user.getElephantUserStatistics().getCardStatistics().put(card, new CardStatistics(cardId));
		}

		user.getElephantUserStatistics().getCardStatistics().get(card).incrementAnsweredRight();

		cardStatisticsService.save(user.getElephantUserStatistics().getCardStatistics().get(card));
		userStatisticsRepositoryService.save(user.getElephantUserStatistics());
		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Incremented Answered Right for Card!")
			.addObject("user", user)
			.build();
	}

	public Response updateRecentlyViewedDecks(ElephantUserStatisticsRequest.UpdateRecentlyViewedDecks request) {
		long deckId = request.getDeckId();
		long userId = request.getUserId();

		ElephantUser user = userService.getUserById(userId);
		Deck deck = deckService.getDeckById(deckId);

		if (user == null || deck == null) {
			return ResponseUtil.getFailureResponse("Invalid User or Deck IDs!", request);
		}

		user.getElephantUserStatistics().getRecentlyViewedDeckIds().remove(deckId);

		user.getElephantUserStatistics().getRecentlyViewedDeckIds().add(0, deckId);

		//at maxed size after adding one

		PropertiesHandler handler = ElephantBackendApplication.ELEPHANT_CONFIG;

		if (user.getElephantUserStatistics().getRecentlyViewedDeckIds().size() == handler.getConfigOption("recentlyViewedDecksMax", Integer::parseInt) + 1) {
			user.getElephantUserStatistics().getRecentlyViewedDeckIds().remove(user.getElephantUserStatistics().getRecentlyViewedDeckIds().size() - 1);
		}

		userStatisticsRepositoryService.save(user.getElephantUserStatistics());
		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Added Deck to Recently Viewed Decks!")
			.addObject("user", user)
			.build();
	}
}
