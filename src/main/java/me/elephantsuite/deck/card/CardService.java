package me.elephantsuite.deck.card;

import java.util.List;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.stats.card.CardStatisticsRepository;
import me.elephantsuite.user.ElephantUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CardService {

	private final CardRepository repository;

	private final CardStatisticsRepository cardStatisticsRepository;

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

	public long deleteDefinitionsByCardId(long id) {
		return repository.deleteCardDefinitionsByCardID(id);
	}

	public long deleteCardById(long id) {
		cardStatisticsRepository.deleteCardStatisticsMapping(id);
		cardStatisticsRepository.deleteCardStatistics(id);
		return repository.deleteCardByID(id);
	}

	public void deleteCardsNotBackpacked(List<Card> cards) {
		cards.forEach(card -> {
			List<Long> backpackIds = repository.backpackIdsMatchWithCard(card.getId());

			if (backpackIds.isEmpty()) {
				repository.deleteCardsFromDeckTable(card.getId());
				deleteCardById(card.getId());
			}
		});
	}

	public List<Card> getAllCards() {
		return repository.getAllCards();
	}
}
