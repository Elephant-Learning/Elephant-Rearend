package me.elephantsuite.deck.card;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CardService {

	private final CardRepository repository;

	public void saveAll(List<Card> cards) {
		this.repository.saveAll(cards);
	}

	public void deleteAll(List<Card> cards) {
		this.repository.deleteAll(cards);
	}
}
