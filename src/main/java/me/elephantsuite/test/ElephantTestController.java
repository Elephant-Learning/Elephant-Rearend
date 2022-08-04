package me.elephantsuite.test;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.DeckRepositoryService;
import me.elephantsuite.response.Response;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
import me.elephantsuite.user.ElephantUserService;
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
}
