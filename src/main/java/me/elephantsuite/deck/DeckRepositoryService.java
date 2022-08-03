package me.elephantsuite.deck;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.deck.card.CardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class DeckRepositoryService {

	private final DeckRepository deckRepository;

	public Deck saveDeck(Deck deck) {
		return deckRepository.save(deck);
	}

	public Deck getDeckById(long id) {
		if (deckRepository.existsById(id)) {
			return deckRepository.getReferenceById(id);
		}

		return null;
	}

	public List<Deck> getAllDecks() {
		return deckRepository.getAllDecks();
	}

	public void deleteDeck(Deck deck, CardService cardService) {
		deck.getCards().forEach(card -> {
			cardService.deleteDefinitionsByCardId(card.getId());
			cardService.deleteCardById(card.getId());
		});

		deckRepository.deleteDeckById(deck.getId());
	}
	public List<Deck> getDecksByUser(long userId) {
		return deckRepository.getDecksByUserId(userId);
	}
}
