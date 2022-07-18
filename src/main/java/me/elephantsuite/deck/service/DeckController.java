package me.elephantsuite.deck.service;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "user/deck")
@AllArgsConstructor
public class DeckController {

	private final DeckService deckService;

	@PostMapping(path = "create")
	public String createDeck(@RequestBody DeckRequest.CreateDeck request) {
		return deckService.createDeck(request);
	}

	@PostMapping(path = "like")
	public String likeDeck(@RequestBody DeckRequest.LikeDeck likeDeck) {
		return deckService.likeDeck(likeDeck);
	}

	@GetMapping(path = "getAll")
	public String getAllDecks() {
		return deckService.getAllDecks();
	}

	@PostMapping(path = "rename")
	public String renameDeck(@RequestBody DeckRequest.RenameDeck renameDeck) {
		return deckService.renameDeck(renameDeck);
	}

	@PutMapping(path = "addTerms")
	public String addTerms(@RequestBody DeckRequest.AddTerms addTerms) {
		return deckService.addTerms(addTerms);
	}

	@DeleteMapping(path = "delete")
	public String deleteDeck(@RequestParam("id") long id) {
		return deckService.deleteDeck(id);
	}
}
