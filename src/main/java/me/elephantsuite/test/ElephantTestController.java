package me.elephantsuite.test;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.DeckRepositoryService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.stats.medal.MedalService;
import me.elephantsuite.user.ElephantUserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@AllArgsConstructor
public class ElephantTestController {

	private ElephantUserService service;

	private DeckRepositoryService deckService;

	private MedalService medalService;

	@PostMapping(path = "testUser")
	public Response testUser(@RequestParam("email") String email) {
		boolean alrRegistered = service.isUserAlreadyRegistered(email);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "tested some stuff")
			.addObject("alrRegistered", alrRegistered)
			.build();
	}

	@PostMapping(path = "testDecksByUserId")
	public Response testDecksByUserId(@RequestParam("user_id") long userId) {
		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "tested some more stuff")
			.addObject("decks", deckService.getDecksByUser(userId))
			.build();
	}

	@DeleteMapping(path = "deleteMedal")
	public Response deleteMedal(@RequestParam("id") long id) {
		medalService.deleteMedal(id);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Deleted Medal!")
			.build();
	}

	@PostMapping(path = "refreshMedals")
	public Response refreshMedals(@RequestParam("id") long id) {
		medalService.updateLoginMedal(ResponseUtil.checkUserValid(id, service).getElephantUserStatistics());

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Did something")
			.build();
	}
}
