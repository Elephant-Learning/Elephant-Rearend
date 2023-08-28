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
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.exception.UserNotEnabledException;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.stats.ElephantUserStatisticsRepositoryService;
import me.elephantsuite.stats.card.CardStatistics;
import me.elephantsuite.stats.card.CardStatisticsService;
import me.elephantsuite.stats.medal.MedalService;
import me.elephantsuite.timeline.Timeline;
import me.elephantsuite.timeline.TimelineRepositoryService;
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

	private final TimelineRepositoryService timelineRepositoryService;

	private final MedalService medalService;

	public Response modifyStatsOnLogin(long id) {
		ElephantUser user = ResponseUtil.checkUserValid(id, userService);

		user.getElephantUserStatistics().incrementDaysStreak(medalService);
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

		ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

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
			throw new InvalidIdException(new Object[]{user, card}, InvalidIdType.CARD, InvalidIdType.USER);
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
			throw new InvalidIdException(new Object[]{user, card}, InvalidIdType.CARD, InvalidIdType.USER);
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
			throw new InvalidIdException(request, InvalidIdType.DECK, InvalidIdType.USER);
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

	public Response updateRecentlyViewedTimelines(ElephantUserStatisticsRequest.UpdateRecentlyViewedTimelines request) {
		long timelineId = request.getTimelineId();
		long userId = request.getUserId();

		ElephantUser user = ResponseUtil.checkUserValid(userId, userService);
		Timeline deck = getTimelineById(timelineId);

		user.getElephantUserStatistics().getRecentlyViewedTimelineIds().remove(timelineId);

		user.getElephantUserStatistics().getRecentlyViewedTimelineIds().add(0, timelineId);

		//at maxed size after adding one

		PropertiesHandler handler = ElephantBackendApplication.ELEPHANT_CONFIG;

		if (user.getElephantUserStatistics().getRecentlyViewedTimelineIds().size() == handler.getConfigOption("recentlyViewedDecksMax", Integer::parseInt) + 1) {
			user.getElephantUserStatistics().getRecentlyViewedTimelineIds().remove(user.getElephantUserStatistics().getRecentlyViewedTimelineIds().size() - 1);
		}

		userStatisticsRepositoryService.save(user.getElephantUserStatistics());
		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Added Timeline to Recently Viewed Timelines!")
			.addObject("user", user)
			.build();
	}

	public Timeline getTimelineById(long id) {
		Timeline tl = timelineRepositoryService.getTimelineById(id);

		if (tl == null) {
			throw new InvalidIdException(id, InvalidIdType.TIMELINE);
		}

		return tl;
	}
}
