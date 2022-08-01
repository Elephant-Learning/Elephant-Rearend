package me.elephantsuite.deck.card;

import java.util.List;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CardService {

	private final CardRepository repository;

	public Card getCardById(long id) {
		if (repository.existsById(id)) {
			return repository.getReferenceById(id);
		}

		return null;
	}
	public void saveAll(List<Card> cards) {
		this.repository.saveAll(cards);
	}

	public void deleteAll(List<Card> cards) {
		this.repository.deleteAll(cards);
	}

	public Card saveCard(Card card) {
		return this.repository.save(card);
	}
}
